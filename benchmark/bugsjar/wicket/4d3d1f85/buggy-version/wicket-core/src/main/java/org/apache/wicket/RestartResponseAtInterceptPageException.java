/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.IWritableRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.ResetResponseException;
import org.apache.wicket.request.handler.PageProvider;
import org.apache.wicket.request.handler.RenderPageRequestHandler;
import org.apache.wicket.request.handler.RenderPageRequestHandler.RedirectPolicy;
import org.apache.wicket.request.http.handler.RedirectRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

/**
 * Causes Wicket to interrupt current request processing and immediately redirect to an intercept
 * page.
 */
public class RestartResponseAtInterceptPageException extends ResetResponseException
{
	private static final long serialVersionUID = 1L;

	/**
	 * Redirects to the specified {@code interceptPage}.
	 * 
	 * @param interceptPage
	 */
	public RestartResponseAtInterceptPageException(Page interceptPage)
	{
		super(new RenderPageRequestHandler(new PageProvider(interceptPage),
			RedirectPolicy.AUTO_REDIRECT));
		InterceptData.set();
	}

	/**
	 * Redirects to the specified intercept page, this will result in a bookmarkable redirect.
	 * 
	 * @param interceptPageClass
	 */
	public RestartResponseAtInterceptPageException(Class<? extends Page> interceptPageClass)
	{
		this(interceptPageClass, null);
	}

	/**
	 * Redirects to the specified intercept page, this will result in a bookmarkable redirect.
	 * 
	 * @param interceptPageClass
	 * @param parameters
	 */
	public RestartResponseAtInterceptPageException(Class<? extends Page> interceptPageClass,
		PageParameters parameters)
	{
		super(new RenderPageRequestHandler(new PageProvider(interceptPageClass, parameters),
			RedirectPolicy.AUTO_REDIRECT));
		InterceptData.set();
	}

	/**
	 * INTERNAL CLASS, DO NOT USE
	 * 
	 * @author igor.vaynberg
	 */
	static class InterceptData implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private Url originalUrl;
		private Map<String, List<StringValue>> postParameters;
		private boolean continueOk;

		public Url getOriginalUrl()
		{
			return originalUrl;
		}

		public Map<String, List<StringValue>> getPostParameters()
		{
			return postParameters;
		}

		public static void set()
		{
			Session session = Session.get();
			session.bind();
			InterceptData data = new InterceptData();
			Request request = RequestCycle.get().getRequest();
			data.originalUrl = request.getOriginalUrl();
			data.postParameters = new HashMap<String, List<StringValue>>();
			for (String s : request.getPostParameters().getParameterNames())
			{
				data.postParameters.put(s, new ArrayList<StringValue>(request.getPostParameters()
					.getParameterValues(s)));
			}
			data.continueOk = false;
			session.setMetaData(key, data);
		}

		public static InterceptData get()
		{
			Session session = Session.get();
			if (session != null)
			{
				return session.getMetaData(key);
			}
			else
			{
				return null;
			}
		}

		public static void clear()
		{
			Session session = Session.get();
			if (session != null)
			{
				session.setMetaData(key, null);
			}
		}

		private static MetaDataKey<InterceptData> key = new MetaDataKey<InterceptData>()
		{
			private static final long serialVersionUID = 1L;
		};
	}

	static boolean continueToOriginalDestination()
	{
		InterceptData data = InterceptData.get();
		if (data != null)
		{
			data.continueOk = true;
			String url = RequestCycle.get().getUrlRenderer().renderUrl(data.originalUrl);
			RequestCycle.get().replaceAllRequestHandlers(new RedirectRequestHandler(url));
			return true;
		}
		return false;
	}

	static IRequestMapper MAPPER = new IRequestMapper()
	{
		public int getCompatibilityScore(Request request)
		{
			return 0;
		}

		public Url mapHandler(IRequestHandler requestHandler)
		{
			return null;
		}

		public IRequestHandler mapRequest(Request request)
		{
			InterceptData data = InterceptData.get();
			if (data != null)
			{
				if (data.originalUrl.equals(request.getOriginalUrl()))
				{
					if (data.postParameters.isEmpty() == false &&
						request.getPostParameters() instanceof IWritableRequestParameters)
					{
						IWritableRequestParameters parameters = (IWritableRequestParameters)request.getPostParameters();
						parameters.reset();
						for (String s : data.postParameters.keySet())
						{
							parameters.setParameterValues(s, data.postParameters.get(s));
						}
					}
					InterceptData.clear();
				}
			}
			return null;
		}
	};
}

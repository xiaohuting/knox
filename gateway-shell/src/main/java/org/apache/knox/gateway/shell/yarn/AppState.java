/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.knox.gateway.shell.yarn;

import com.jayway.jsonpath.JsonPath;
import org.apache.knox.gateway.shell.AbstractRequest;
import org.apache.knox.gateway.shell.BasicResponse;
import org.apache.knox.gateway.shell.KnoxSession;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.util.concurrent.Callable;

public class AppState {

    public static class Request extends AbstractRequest<Response> {

        private String appId;

        Request(KnoxSession session) {
            super(session);
        }

        public Request appId(String appId) {
            this.appId = appId;
            return this;
        }

        @Override
        protected Callable<Response> callable() {
            return new Callable<Response>() {
                @Override
                public Response call() throws Exception {
                    URIBuilder uri = uri( Yarn.SERVICE_PATH, "/v1/cluster/apps/", appId, "/state" );
                    HttpGet request = new HttpGet( uri.build() );
                    return new Response( execute( request ) );
                }
            };
        }
    }

    public static class Response extends BasicResponse {

        Response( HttpResponse response ) {
            super( response );
        }

        public String getState() throws IOException {
            return JsonPath.read(getString(), "$.state");
        }
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cdancy.bitbucket.rest.features;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;

import com.cdancy.bitbucket.rest.BitbucketApi;
import com.cdancy.bitbucket.rest.BitbucketApiMetadata;
import com.cdancy.bitbucket.rest.domain.PagedResponse;
import com.cdancy.bitbucket.rest.domain.pullrequest.comments.PullRequestComment;
import com.cdancy.bitbucket.rest.internal.BaseBitbucketMockTest;
import com.cdancy.bitbucket.rest.options.Anchor;
import com.cdancy.bitbucket.rest.options.CreatePullRequestComment;
import com.cdancy.bitbucket.rest.options.FileType;
import com.cdancy.bitbucket.rest.options.LineType;
import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

/**
 * Mock tests for the {@link PullRequestCommentsApi} class.
 */
@Test(groups = "unit", testName = "PullRequestCommentsApiMockTest")
public class PullRequestCommentsApiMockTest extends BaseBitbucketMockTest {

    public void testGetPullRequestComments() throws Exception {
        MockWebServer server = mockEtcdJavaWebServer();

        server.enqueue(new MockResponse().setBody(
                payloadFromResource("/pull-request-comments.json"))
                .setResponseCode(200));
        BitbucketApi baseApi = api(server.getUrl("/"));
        PullRequestCommentsApi api = baseApi.pullRequestCommentsApi();
        try {
            PagedResponse<PullRequestComment> actualResponse = PullRequestCommentsApiGetBuilder
                    .get()
                    .withProject("PRJ")
                    .withRepo("repo")
                    .withPullRequestId(1)
                    .withPath("basic_branching/file.txt")
                    .withStart(0)
                    .withLimit(10)
                    .invoke(api);

            assertThat(actualResponse.getValues()).hasSize(1);

            assertThat(actualResponse.getValues().get(0).text()).isEqualTo(
                    "in diff comment");

            assertSent(
                    server,
                    "GET",
                    "/rest/api/"
                            + BitbucketApiMetadata.API_VERSION
                            + "/projects/PRJ/repos/repo/pull-requests/1/comments/?path=basic_branching/file.txt&start=0&limit=10");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testPostPullRequestComments() throws Exception {
        MockWebServer server = mockEtcdJavaWebServer();

        server.enqueue(new MockResponse().setBody(
                payloadFromResource("/pull-request-comments-post.json"))
                .setResponseCode(201));
        BitbucketApi baseApi = api(server.getUrl("/"));
        PullRequestCommentsApi api = baseApi.pullRequestCommentsApi();
        try {
            CreatePullRequestComment createPullRequestComment = CreatePullRequestComment
                    .create("A text here",
                            Anchor.create(1, LineType.ADDED, FileType.TO, ""));

            PullRequestComment actualResponse = PullRequestCommentsApiPostBuilder
                    .post()
                    .withProject("PRJ")
                    .withRepo("repo")
                    .withPullRequestId(1)
                    .withCreatePullRequestComment(createPullRequestComment)
                    .invoke(api);

            assertThat(actualResponse).isNotNull();

            assertThat(actualResponse.text()).isEqualTo(
                    "in diff comment");

            assertSent(
                    server,
                    "POST",
                    "/rest/api/"
                            + BitbucketApiMetadata.API_VERSION
                            + "/projects/PRJ/repos/repo/pull-requests/1/comments/");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

    public void testDeletePullRequestComments() throws Exception {
        MockWebServer server = mockEtcdJavaWebServer();

        server.enqueue(new MockResponse()
                .setResponseCode(204));
        BitbucketApi baseApi = api(server.getUrl("/"));
        PullRequestCommentsApi api = baseApi.pullRequestCommentsApi();
        try {

            PullRequestCommentsApiDeleteBuilder
                    .delete()
                    .withProject("PRJ")
                    .withRepo("repo")
                    .withPullRequestId(1)
                    .withVersion(1)
                    .invoke(api);

            assertSent(
                    server,
                    "DELETE",
                    "/rest/api/"
                            + BitbucketApiMetadata.API_VERSION
                            + "/projects/PRJ/repos/repo/pull-requests/1/comments/?version=1");
        } finally {
            baseApi.close();
            server.shutdown();
        }
    }

}

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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.cdancy.bitbucket.rest.BaseBitbucketApiLiveTest;
import com.cdancy.bitbucket.rest.domain.PagedResponse;
import com.cdancy.bitbucket.rest.domain.project.Project;
import com.cdancy.bitbucket.rest.domain.pullrequest.comments.PullRequestComment;
import com.cdancy.bitbucket.rest.domain.repository.Repository;
import com.cdancy.bitbucket.rest.options.Anchor;
import com.cdancy.bitbucket.rest.options.CreateProject;
import com.cdancy.bitbucket.rest.options.CreatePullRequestComment;
import com.cdancy.bitbucket.rest.options.CreateRepository;
import com.cdancy.bitbucket.rest.options.FileType;
import com.cdancy.bitbucket.rest.options.LineType;

//@Test(groups = "live", testName = "PullRequestCommentsApiLiveTest", singleThreaded = true)
public class PullRequestCommentsApiLiveTest extends BaseBitbucketApiLiveTest {

    private Repository repo;

    /**
     * Create the project and repo and start work with it.
     */
    @BeforeClass
    public void before() {
        String projectKey = "PROJECT_2";
        CreateProject createProject = CreateProject.create(projectKey, null,
                null, null);
        Project project = api.projectApi().get(projectKey);
        if (project.errors().isEmpty()) {
            api.projectApi().delete(project.key());
        }
        project = api.projectApi().create(createProject);

        String repoSlug = "rep_2";
        CreateRepository createRepository = CreateRepository.create(repoSlug,
                false);
        repo = api.repositoryApi().create(project.key(), createRepository);
    }

    public void testPostPullRequestComment() throws Exception {
        CreatePullRequestComment createPullRequestComment = CreatePullRequestComment
                .create("A text here",
                        Anchor.create(1, LineType.ADDED, FileType.TO, ""));

        PullRequestCommentsApiPostBuilder.post()
                .withProject(repo.project().key())
                .withRepo(repo.slug())
                .withPullRequestId(1)
                .withCreatePullRequestComment(createPullRequestComment)
                .invoke(api.pullRequestCommentsApi());
    }

    @Test(dependsOnMethods = "testPostPullRequestComment")
    public void testGetPullRequestComments() throws Exception {
        PagedResponse<PullRequestComment> actualResponse = PullRequestCommentsApiGetBuilder
                .get()
                .withProject(repo.project().key())
                .withRepo(repo.slug())
                .withPullRequestId(1)
                .withPath("basic_branching/file.txt")
                .withStart(0)
                .withLimit(10)
                .invoke(api.pullRequestCommentsApi());

        assertThat(actualResponse.getValues()).hasSize(1);
        assertThat(actualResponse.getValues().get(0).text()).isEqualTo(
                "in diff comment");
    }
}

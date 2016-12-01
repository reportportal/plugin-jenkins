/*
 * Copyright 2016 EPAM Systems
 *
 *
 * This file is part of EPAM Report Portal.
 * https://github.com/reportportal/plugin-jenkins
 *
 * Report Portal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Report Portal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Report Portal.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.epam.reportportal

import groovy.json.JsonSlurper
import hudson.Extension
import hudson.Launcher
import hudson.model.AbstractBuild
import hudson.model.AbstractProject
import hudson.model.Action
import hudson.model.BuildListener
import hudson.tasks.BuildStepDescriptor
import hudson.tasks.BuildStepMonitor
import hudson.tasks.Publisher
import hudson.tasks.Recorder
import hudson.util.FormValidation
import org.kohsuke.stapler.DataBoundConstructor
import org.kohsuke.stapler.QueryParameter

class ResultRecorder extends Recorder {

    String endpoint
    String project
    String launch
    String token

    @DataBoundConstructor
    ResultRecorder(String endpoint, String project, String launch, String token) {
        this.endpoint = endpoint
        this.project = project
        this.launch = launch
        this.token = token
    }

    @Override
    BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE
    }

    @Override
    BuildStepDescriptor<Publisher> getDescriptor() {
        (RPDescriptor) super.getDescriptor()
    }

    @Override
    boolean perform(AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener) throws InterruptedException, IOException {
        try {
            def text = new URL(endpoint + "/api/v1/" + project + '/launch?page.page=1&page.size=1&page.sort=start_time,DESC&filter.eq.name=' + URLEncoder.encode(this.launch, 'UTF-8')).getText(requestProperties: ['Authorization': 'bearer ' + token])
            def launchJson = new JsonSlurper().parseText(text)
            def executions = launchJson.content.statistics.executions
            Launch launch = new Launch().with {
                passed = executions.passed[0] as int
                failed = executions.failed[0] as int
                skipped = executions.skipped[0] as int
                name = launchJson.content.name[0]
                launchNumber = launchJson.content.number[0] as int
                status = launchJson.content.status[0]
                startTime = launchJson.content.start_time[0] as long
                uiLink = endpoint + "/#" + project + "/launches/all/" + launchJson.content.id[0]
                it
            }
            build.addAction(new RPBuildAction(build, launch))
            launchJson.content.status == 'PASSED'
        } catch (Exception e) {
            e.printStackTrace()
            false
        }

    }

    @Override
    Action getProjectAction(AbstractProject<?, ?> project) {
        new RPProjectAction(project)
    }

    @Extension
    static final class RPDescriptor extends BuildStepDescriptor<Publisher> {

        @Override
        boolean isApplicable(Class jobType) {
            true
        }

        @Override
        String getDisplayName() {
            "ReportPortal Results Publisher"
        }

        FormValidation doCheckConnection(
                @QueryParameter("endpoint") String endpoint,
                @QueryParameter("project") String project,
                @QueryParameter("launch") String launch,
                @QueryParameter("token") String token) {
            try {
                new URL(endpoint + "/api/v1/" + project + '/launch?page.page=1&page.size=1&page.sort=start_time,DESC&filter.eq.name='
                        + URLEncoder.encode(launch, 'UTF-8'))
                        .getText(requestProperties: ['Authorization': 'bearer ' + token])
                FormValidation.okWithMarkup('Connection successfully established')
            } catch (Exception e) {
                FormValidation.error('Something wrong with parameters')
            }
        }
    }
}

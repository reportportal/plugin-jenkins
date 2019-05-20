# plugin-jenkins
Jenkins CI Plugin - represents statistics and visualization at Jenkins job page

Important Disclaimer: 
>you don't need to have jenkins plugin for ReportPortal, in order to have results in ReportPortal from tests, executed by Jenkins job. RP adapter connects to test engine (like junit, testNG, nunit, etc.) and fires events, which sends data into RP server. And there is no any difference where you execute results, at local env, remote env, or with jenkins, RP adapter will send data directly into RP server.

>this plugin just shows quick link to corresponding execution result in ReportPortal, and simple trend on chart

### https://wiki.jenkins.io/display/JENKINS/ReportPortal+Plugin

ReportPortal Plugin is a plugin for representing a statistics and a test results visualization of already launched builds on the Jenkins job page. Plugin builds a trend chart that is based on finished launches that have been run from the moment of a plugin installation. It allows you to take a quick overlook of a builds results structure and make a firstly estimation of results. If you follow a link that is also provided by a plugin, you will be redirected to the launch on ReportPortal page where you can continue a deeper investigations. You can read more about a plugin on GitHub ReportPortal Jenkins Plugin.

![alt text](https://wiki.jenkins.io/download/attachments/133958181/image2017-11-10%2017%3A51%3A47.png?version=1&modificationDate=1510325508000&api=v2 "Logo Title Text 1")


Build instruction:

1. Checkout source code
2. Open console and move to root folder
3. Perform command './gradlew clean jpi'
4. Verify that reportportal.hpi file appeared in build/libs/ directory
5. Upload received file to Jenkins using following instruction https://jenkins.io/doc/book/managing/plugins/#from-the-web-ui-2

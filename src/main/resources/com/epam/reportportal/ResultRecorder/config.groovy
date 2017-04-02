package com.epam.reportportal.ResultRecorder

f = namespace('lib/form')

f.section(title: 'Parameters') {
    f.entry(title: "Rest endpoint") {
        f.textbox(name: "endpoint", field: 'endpoint')
    }
    f.entry(title: "Project") {
        f.textbox(name: "project", field: 'project')
    }
    f.entry(title: "Launch") {
        f.textbox(name: "launch", field: 'launch')
    }
    f.entry(title: "Token") {
        f.textbox(name: "token", field: 'token')
    }
    f.validateButton(title: "Check connection", method:'checkConnection', with: 'endpoint,project,launch,token')
}

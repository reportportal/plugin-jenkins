package com.epam.reportportal.RPProjectAction

l = namespace(lib.LayoutTagLib)
st = namespace("jelly:stapler")

l.layout(title: "ReportPortal Results") {
    st.include(page: "sidepanel.jelly", it: my.project)
    l.main_panel() {
        h1("ReportPortal Results Chart")
        img(src: "graph")
    }
}
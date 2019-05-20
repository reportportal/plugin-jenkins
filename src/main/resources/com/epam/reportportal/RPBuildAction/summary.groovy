t = namespace("/lib/hudson")

t.summary(icon: "/plugin/reportportal/img/RP_ic_100.svg") {
    a(href: "${my.launch.uiLink}", target: 'blank') {
        text("${my.launch.name}")
    }
    p()
    b("Report Portal launch info:")
    ul() {
        li("Name: ${my.launch.name}")
        li("Number: ${my.launch.launchNumber}")
        li("Status: ${my.launch.status}")
        li("Start time: ${new Date(my.launch.startTime)}")
    }
    p()
    b("Launch Statistics:")
    ul() {
        li("Passed Tests: ${my.launch.passed}")
        li("Failed Tests: ${my.launch.failed}")
        li("Skipped Tests: ${my.launch.skipped}")
    }
}
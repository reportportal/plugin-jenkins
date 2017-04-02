t = namespace("/lib/hudson")

t.summary(icon: "/plugin/rp4jenkins/img/RP_ic_100.svg") {
    a(href: "${my.launch.uiLink}", target: 'blank') {
        text("${my.launch.name}")
    }
    p()
    b("Launch Statistics:")
    ul() {
        li("Passed Tests: ${my.launch.passed}")
        li("Failed Tests: ${my.launch.failed}")
        li("Skipped Tests: ${my.launch.skipped}")
    }
}
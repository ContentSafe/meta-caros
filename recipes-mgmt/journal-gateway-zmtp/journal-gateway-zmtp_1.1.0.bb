SUMMARY = "journald gateway for ZMTP based log forwarding"
SECTION = "logging"
DEPENDS += "zeromq"
DEPENDS += "czmq"
DEPENDS += "jansson"
DEPENDS += "systemd"
LICENSE = "LGPL-2.1"
PR = "r6"

LIC_FILES_CHKSUM = "file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c"

inherit autotools systemd

SRC_URI = "git://github.com/contentsafe/journal-gateway-zmtp.git;protocol=git;branch=logrotate"
SRCREV="${AUTOREV}"


S = "${WORKDIR}/git"

SYSTEMD_AUTO_ENABLE = "disable"
SYSTEMD_SERVICE_${PN} = "${PN}-sink.service"
SYSTEMD_SERVICE_${PN} += "${PN}-source.service"
SYSTEMD_SERVICE_${PN} += "remove_old_logs.timer"

do_compile() {
    echo ${S}
    cd ${S}
    oe_runmake
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/journal-gateway-zmtp-source ${D}${bindir}
    install -m 0755 ${S}/journal-gateway-zmtp-sink ${D}${bindir}
    install -m 0755 ${S}/journal-gateway-zmtp-control ${D}${bindir}

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${S}/misc/journal-gateway-zmtp-sink.service ${D}${systemd_unitdir}/system/.
    install -m 0644 ${S}/misc/journal-gateway-zmtp-source.service ${D}${systemd_unitdir}/system/.
    install -m 0644 ${S}/misc/remove_old_logs.service ${D}${systemd_unitdir}/system/.

    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/misc/journal-gateway-zmtp-sink.conf ${D}${sysconfdir}/.
    install -m 0644 ${S}/misc/journal-gateway-zmtp-source.conf ${D}${sysconfdir}/.
    install -m 0644 ${S}/misc/remove_old_logs.conf ${D}${sysconfdir}/.

    install -m 0644 ${S}/misc/remove_old_logs.timer ${D}${systemd_unitdir}/system/.
}

FILES_${PN} = "${bindir}/journal-gateway-zmtp-source ${bindir}/journal-gateway-zmtp-sink ${bindir}/journal-gateway-zmtp-control"

# systemd units
FILES_${PN} += "${systemd_unitdir}/system/journal-gateway-zmtp-sink.service"
FILES_${PN} += "${systemd_unitdir}/system/journal-gateway-zmtp-source.service"
FILES_${PN} += "${systemd_unitdir}/system/remove_old_logs.service"
FILES_${PN} += "${systemd_unitdir}/system/remove_old_logs.timer"

# config files
FILES_${PN} += "${sysconfdir}/journal-gateway-zmtp-sink.conf"
FILES_${PN} += "${sysconfdir}/journal-gateway-zmtp-source.conf"
FILES_${PN} += "${sysconfdir}/remove_old_logs.conf"
CONFFILES_${PN} += "${sysconfdir}/journal-gateway-zmtp-sink.conf"
CONFFILES_${PN} += "${sysconfdir}/journal-gateway-zmtp-source.conf"
CONFFILES_${PN} += "${sysconfdir}/remove_old_logs.conf"

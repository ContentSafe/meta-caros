HOMEPAGE = "http://elixir-lang.org/"
SUMMARY  = "Elixir is a dynamic, functional language designed for building scalable and maintainable applications"
DESCRIPTION = "Elixir leverages the Erlang VM, known for running low-latency, distributed and fault-tolerant systems, while also being successfully used in web development and the embedded software domain."
LICENSE  = "GPLv2"

PR = "r1"

LIC_FILES_CHKSUM = "file://LICENSE;md5=0c48e31d655fb0e9b1f60b931e652f47"

SRC_URI = "https://github.com/elixir-lang/elixir/archive/v${PV}.tar.gz;downloadfilename=${PN}-${PV}.tar.gz"
SRC_URI[md5sum] = "abbdda96891b4519a1dc3e65b58f1b0b"
SRC_URI[sha256sum] = "5ce5c226b3d11d751b41ad79b915b86f13f8a1b89ef3e733321d3f46ff4d81b8"

PV_hex = "0.7.5"
SRC_URI += " https://github.com/hexpm/hex/archive/v${PV_hex}.tar.gz;name=hex"

SRC_URI[hex.md5sum] = "052ba46cfb602032237718bcf8bad96d"
SRC_URI[hex.sha256sum] = "22403952073cee120894a50fe26babd76d6ce3d7f21d08d34973a8c21d012c14"

DEPENDS = "erlang"
RDEPENDS_${PN} += "erlang"

S = "${WORKDIR}/${PN}-${PV}"

do_install() {
   install -d ${D}${bindir}
   install -d ${D}${libdir}

   install -m 755 ${S}/bin/elixir ${D}${bindir}
   install -m 755 ${S}/bin/elixirc ${D}${bindir}
   install -m 755 ${S}/bin/iex ${D}${bindir}
   install -m 755 ${S}/bin/mix ${D}${bindir}

   for dir in `ls ${S}/lib`; do
       install -d ${D}${libdir}/$dir
       cp -fr ${S}/lib/$dir/ebin ${D}${libdir}/$dir/
   done

   export PATH="${D}${bindir}:$PATH"
   cd "${WORKDIR}/hex-${PV_hex}"
   MIX_ENV=release mix compile
   cp -rv _build/release/lib/hex ${D}${libdir}/hex/
}

FILES_${PN} = "${bindir} ${libdir}"

BBCLASSEXTEND = "native"

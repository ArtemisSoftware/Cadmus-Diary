object Module {

    private const val data = ":data"
    private const val domain = ":domain"
    private const val presentation = ":presentation"
    private const val ui = ":ui"

    private const val feature = ":feature"
    private const val core = ":core"

    const val coreData = "${core}${data}"
    const val coreDomain = "${core}${domain}"
    const val coreUi = "${core}${ui}"

    private const val home = ":home"

    const val homeDomain = "${feature}${home}${domain}"
    const val homePresentation = "${feature}${home}${presentation}"

    private const val write = ":write"

    const val writeDomain = "${feature}${write}${domain}"
    const val writePresentation = "${feature}${write}${presentation}"

    private const val authentication = ":authentication"

    const val authenticationData = "${feature}${authentication}${data}"
    const val authenticationDomain = "${feature}${authentication}${domain}"
    const val authenticationPresentation = "${feature}${authentication}${presentation}"


    const val util = ":util"
    const val navigation = ":navigation"
}
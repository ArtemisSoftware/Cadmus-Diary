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

    const val homePresentation = "${feature}${home}${presentation}"
}
package org.weyoung.stockcaculator.data.remote

import java.net.URLEncoder


val keywords = URLEncoder.encode("芯片，创业板")

val webPageUrl =
    "https://www.iwencai.com/stockpick/search?ts=1&f=1&qs=stockhome_topbar_click&w=$keywords"

fun wholePageUrl(token: String) =
    "https://www.iwencai.com/stockpick/cache?token=$token&p=1&perpage=70&changeperpage=1&showType=[%22%22,%22%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22]"

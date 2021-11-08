package org.weyoung.stockcalculator.data.remote

import java.net.URLEncoder


val keywords = URLEncoder.encode("上日涨停，非ST，非新股，十大流通股东占比，成交量，竞价涨幅")

const val homePageUrl = "https://www.iwencai.com/stockpick/search?"

const val loadMoreUrl = "https://www.iwencai.com/stockpick/cache?token="

val webPageUrl = "${homePageUrl}ts=1&f=1&qs=stockhome_topbar_click&w=$keywords"

fun wholePageUrl(token: String) =
    "$loadMoreUrl$token&p=1&perpage=70&changeperpage=1&showType=[%22%22,%22%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22,%22onTable%22]"

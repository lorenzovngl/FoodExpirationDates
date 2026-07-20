package com.lorenzovainigli.news.data.remote.parser

import com.lorenzovainigli.news.domain.model.NewsItem
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RssNewsParser {

    fun parse(xml: String): List<NewsItem> {
        val parser = XmlPullParserFactory.newInstance().newPullParser()
        parser.setInput(StringReader(xml))

        val news = mutableListOf<NewsItem>()

        var insideItem = false
        var title: String? = null
        var link: String? = null
        var description: String? = null
        var guid: String? = null
        var pubDate: String? = null

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> {
                    when (parser.name) {
                        "item" -> insideItem = true
                        "title" -> if (insideItem) title = parser.nextText()
                        "link" -> if (insideItem) link = parser.nextText()
                        "description" -> if (insideItem) description = parser.nextText()
                        "guid" -> if (insideItem) guid = parser.nextText()
                        "pubDate" -> if (insideItem) pubDate = parser.nextText()
                    }
                }

                XmlPullParser.END_TAG -> {
                    if (parser.name == "item") {
                        val finalUrl = link.orEmpty()

                        if (!title.isNullOrBlank() && finalUrl.isNotBlank()) {
                            news += NewsItem(
                                id = guid ?: finalUrl,
                                title = title,
                                description = description,
                                url = finalUrl,
                                publishedAt = pubDate?.toInstantOrNull(),
                                isRead = false
                            )
                        }

                        insideItem = false
                        title = null
                        link = null
                        description = null
                        guid = null
                        pubDate = null
                    }
                }
            }

            parser.next()
        }

        return news
    }

    private fun String.toInstantOrNull() = runCatching {
        ZonedDateTime.parse(
            this,
            DateTimeFormatter.RFC_1123_DATE_TIME.withLocale(Locale.ENGLISH)
        ).toInstant()
    }.getOrNull()
}
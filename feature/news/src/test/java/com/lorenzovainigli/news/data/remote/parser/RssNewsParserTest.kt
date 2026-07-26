package com.lorenzovainigli.news.data.remote.parser

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant

class RssNewsParserTest {

    private lateinit var parser: RssNewsParser

    @Before
    fun setUp() {
        parser = RssNewsParser()
    }

    private fun readResource(path: String): String {
        return requireNotNull(javaClass.classLoader?.getResource(path))
            .readText()
    }

    @Test
    fun `parse parses valid rss item`() {
        // Given
        val xml = readResource("rss/valid_single_item.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)

        val article = result.first()

        assertEquals("article-1", article.id)
        assertEquals("First article", article.title)
        assertEquals("Article description", article.description)
        assertEquals("https://example.com/1", article.url)
        assertEquals(
            Instant.parse("2025-07-08T12:00:00Z"),
            article.publishedAt
        )
        assertFalse(article.isRead)
    }

    @Test
    fun `parse parses multiple rss items`() {
        // Given
        val xml = readResource("rss/valid_multiple_items.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(2, result.size)

        assertEquals("article-1", result[0].id)
        assertEquals("First article", result[0].title)
        assertEquals("https://example.com/1", result[0].url)

        assertEquals("article-2", result[1].id)
        assertEquals("Second article", result[1].title)
        assertEquals("https://example.com/2", result[1].url)
    }

    @Test
    fun `parse uses guid as id when present`() {
        // Given
        val xml = readResource("rss/guid_as_id.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)
        assertEquals("custom-guid-123", result.first().id)
    }

    @Test
    fun `parse uses link as id when guid is missing`() {
        // Given
        val xml = readResource("rss/link_as_id.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)
        assertEquals(
            "https://example.com/article",
            result.first().id
        )
    }

    @Test
    fun `parse skips item without title`() {
        // Given
        val xml = readResource("rss/missing_title.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parse skips item without link`() {
        // Given
        val xml = readResource("rss/missing_link.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `parse keeps null description when missing`() {
        // Given
        val xml = readResource("rss/missing_description.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)
        assertNull(result.first().description)
    }

    @Test
    fun `parse parses valid publication date`() {
        // Given
        val xml = readResource("rss/valid_pub_date.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)
        assertEquals(
            Instant.parse("2025-07-08T12:00:00Z"),
            result.first().publishedAt
        )
    }

    @Test
    fun `parse returns null publication date when invalid`() {
        // Given
        val xml = readResource("rss/invalid_pub_date.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)
        assertNull(result.first().publishedAt)
    }

    @Test
    fun `parse ignores channel metadata`() {
        // Given
        val xml = readResource("rss/channel_metadata.xml")

        // When
        val result = parser.parse(xml)

        // Then
        assertEquals(1, result.size)

        val article = result.first()

        assertEquals("Article title", article.title)
        assertEquals("https://example.com/article", article.url)
    }
}
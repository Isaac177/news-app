package com.example.news.data

import com.example.news.model.NewsArticle
import java.util.Date
import java.util.UUID
import kotlin.random.Random

object MockData {
    private val loremIpsum = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.
    """.trimIndent()

    private val authors = listOf(
        "Emma Thompson", "Michael Chen", "Sophia Rodriguez", "Ahmed Hassan",
        "Olivia Nguyen", "Daniel Kim", "Isabella Müller", "Raj Patel"
    )

    private val titles = listOf(
        "Global Economic Summit Concludes with Landmark Agreement",
        "Breakthrough in Renewable Energy Technology Announced",
        "Major Climate Change Report Signals Urgent Action Needed",
        "Tech Giants Face Antitrust Scrutiny in Congressional Hearing",
        "International Space Station Marks 25 Years in Orbit",
        "New Study Reveals Surprising Health Benefits of Coffee",
        "Global Education Initiative Aims to Bridge Digital Divide",
        "Archaeologists Uncover Ancient City in Remote Desert Region",
        "Artificial Intelligence Milestone: AI Passes Medical Licensing Exam",
        "World Leaders Gather for Crucial Cybersecurity Summit"
    )

    fun getNewsArticles(): List<NewsArticle> {
        return List(20) { index ->
            val id = UUID.randomUUID().toString()
            val contentLength = Random.nextInt(300, 1000)
            NewsArticle(
                id = id,
                title = titles[index % titles.size],
                description = loremIpsum.take(150) + "...",
                content = loremIpsum.repeat((contentLength / loremIpsum.length) + 1).take(contentLength),
                author = authors[Random.nextInt(authors.size)],
                publishedAt = Date(System.currentTimeMillis() - Random.nextLong(0, 30L * 24 * 60 * 60 * 1000)),
                imageUrl = "https://picsum.photos/800/600"
            )
        }
    }
}
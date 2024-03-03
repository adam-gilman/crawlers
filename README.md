# Norconex Crawlers

Norconex web and filesystem crawlers are full-featured crawlers (or spider) that can manipulate and store collected data in a repository of your choice (e.g., a search engine). They are very flexible, powerful, easy to extend, and portable. They can be used command-line with file-based configuration on any OS or embedded into Java applications using well-documented APIs.

Visit the website for binary downloads and documentation:
https://opensource.norconex.com/crawlers/

# Are you on the right branch?

This branch holds version 4 code, which is still in development.

**For the latest stable release of Norconex Web Crawler, use the [version 3 branch](https://github.com/Norconex/crawlers/tree/3.x-branch).**

# UPCOMING: Crawler V4 Stack

[![Java CI with Maven](https://github.com/Norconex/crawlers/actions/workflows/ci.yml/badge.svg)](https://github.com/Norconex/crawlers/actions/workflows/ci.yml)

As of Feb 24, 2024, the default `main` branch holds code for the upcoming version 4 crawler stack. It is now a mono-repo containing all Norconex crawler-related projects previously maintained in their own repos. All projects in this mono report will now be released simultaneously and share the same version number.

Until v4 is officially released, this branch should not be considered stable.  

## Projects

| Folder                       | Artifact Id                    | Build         |
| ---------------------------- | ------------------------------ | ------------- |
| crawler/core/                | nx-crawler-core test           | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-crawler-core&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-crawler-core) |
| crawler/web/                 | nx-crawler-web                 | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-crawler-web&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-crawler-web) |
| crawler/fs/                  | nx-crawler-fs                  | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-crawler-fs&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-crawler-fs) |
| importer/                    | nx-importer                    | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-importer&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-importer) |
| committer/core/              | nx-committer-core              | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-committer-core&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-committer-core) |
| committer/amazoncloudsearch/ | nx-committer-amazoncloudsearch | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-committer-amazoncloudsearch&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-committer-amazoncloudsearch) |
| committer/idol/              | nx-committer-idol              | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-committer-idol&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-committer-idol) |
| committer/elasticsearch/     | nx-committer-elasticsearch     | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-committer-elasticsearch&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-committer-elasticsearch) |
| committer/solr/              | nx-committer-solr              | [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.norconex.crawler%3Anx-committer-solr&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=com.norconex.crawler%3Anx-committer-solr) |

All projects in this repository share the same Maven group id:

    com.norconex.crawler

package com.kevinkirwansoftware.capsule.throwaway;

data class BookListModel(val items: List<VolumeInfo>)
data class VolumeInfo(val volumeInfo: BookInfo)
data class BookInfo(val title: String, val publisher: String, val description: String, val imageLinks: ImageLinks)
data class ImageLinks(val smallThumbnail: String)
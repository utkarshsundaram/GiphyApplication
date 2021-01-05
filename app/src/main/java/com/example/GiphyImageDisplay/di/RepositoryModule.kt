package com.example.GiphyImageDisplay.di

import com.example.GiphyImageDisplay.datasource.repository.GiphyRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory { GiphyRepository(get()) }
}
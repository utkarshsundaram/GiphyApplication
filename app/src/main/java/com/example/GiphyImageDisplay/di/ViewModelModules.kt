package com.example.GiphyImageDisplay.di

import com.example.GiphyImageDisplay.viemodel.GiphyViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { GiphyViewModel(get()) }
}
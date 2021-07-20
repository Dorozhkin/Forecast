package com.example.forecast.model.suggestions

import android.content.Context
import com.example.forecast.R
import com.paulrybitskyi.persistentsearchview.adapters.model.SuggestionItem
import com.paulrybitskyi.persistentsearchview.model.Suggestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SuggestionsSource(private val context: Context) {

    private val suggestions = context.resources.getStringArray(R.array.cities)

    fun retrieveSuggestionsList(query: String): Flow<List<SuggestionItem>> {
        return flow {
            val items = mutableListOf<SuggestionItem>()
            if(query.length > 2) {
                for (s in suggestions) {
                    if (s.toLowerCase().startsWith(query.toLowerCase())) {
                        items.add(SuggestionItem(Suggestion().setText(s)))
                    }
                }
                emit(items)
            } else {
                emit(emptyList())
            }
        }
    }
}
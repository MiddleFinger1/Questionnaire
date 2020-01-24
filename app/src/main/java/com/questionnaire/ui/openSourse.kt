package com.questionnaire.ui

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import com.Helper
import com.questionnaire.Source
import com.questionnaire.Source.Companion.TYPE_DOCUMENT
import com.questionnaire.Source.Companion.TYPE_IMAGE
import com.questionnaire.Source.Companion.TYPE_LINK
import com.questionnaire.Source.Companion.TYPE_SONG
import java.io.InputStream


fun openSource(activity: AppCompatActivity, source: Source): Any? =
	when (source.type){
			TYPE_IMAGE -> {
				val input: InputStream
				if (source.isInSd) {
					input = activity.assets.open(source.path)
					Drawable.createFromStream(input, null)
				}
				else null
			}
		    TYPE_DOCUMENT -> {
				if (source.isInSd)
					Helper.stream2file(activity.assets.open(source.path))
				else null
			}
		    TYPE_SONG -> {
				null
			}
            TYPE_LINK -> source.path
			else -> null
	}

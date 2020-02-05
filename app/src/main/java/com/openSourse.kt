package com

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import com.Helper
import com.json.questionnaire.Source
import com.json.questionnaire.Source.Companion.TYPE_DOCUMENT
import com.json.questionnaire.Source.Companion.TYPE_IMAGE
import com.json.questionnaire.Source.Companion.TYPE_LINK
import com.json.questionnaire.Source.Companion.TYPE_SONG
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

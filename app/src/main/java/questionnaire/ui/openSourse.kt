package questionnaire.ui

import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import questionnaire.Source
import questionnaire.Source.Companion.TYPE_DOCUMENT
import questionnaire.Source.Companion.TYPE_IMAGE
import questionnaire.Source.Companion.TYPE_LINK
import questionnaire.Source.Companion.TYPE_SONG
import java.io.InputStream


fun openSourse(activity: AppCompatActivity, source: Source): Any? =
	when (source.type){
			TYPE_IMAGE -> {
				val input: InputStream
				if (source.isInSd) {
					input = activity.assets.open(source.path)
					Drawable.createFromStream(input, null)
				}
				else null
					//
			}
		    TYPE_DOCUMENT -> {
				null
			}
		    TYPE_SONG -> {
				null
			}
            TYPE_LINK -> source.path
			else -> null
	}

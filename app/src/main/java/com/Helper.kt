package com

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import org.apache.commons.io.IOUtils
import java.io.*
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Calendar.*


object Helper {

    fun converting(inputStream: InputStream) =
        try {
            val bis = BufferedInputStream(inputStream)
            val buf = ByteArrayOutputStream()
            var result = bis.read()
            while (result != -1) {
                buf.write(result.toByte().toInt())
                result = bis.read()
            }
            buf.toString()
        } catch (ex: Exception) {
            ""
        }

    fun getRealPathFromURI(context: Context, uri: Uri): String{
        var cursor: Cursor? = null
        try {
            val prog = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, prog, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }

    infix operator fun Calendar.plus(calendar: Calendar): Calendar {
        this[SECOND] += calendar[SECOND]
        this[MINUTE] += calendar[MINUTE]
        this[HOUR] += calendar[HOUR]
        this[DAY_OF_MONTH] += calendar[DAY_OF_MONTH]
        this[MONTH] += calendar[MONTH]
        this[YEAR] += calendar[YEAR]
        return this
    }

    fun calendarToString(calendar: Calendar?) =
        if (calendar != null) "${calendar[SECOND]}.${calendar[MINUTE]}.${calendar[HOUR]}." +
                "${calendar[DAY_OF_MONTH]}.${calendar[MONTH]}.${calendar[YEAR]}"
        else ""

    fun stringToCalendar(string: String) =
        GregorianCalendar().apply {
            val measures = string.split(".")
            if (measures.size == 6) {
                try {
                    this[SECOND] = measures[0].toInt()
                    this[MINUTE] = measures[1].toInt()
                    this[HOUR] = measures[2].toInt()
                    this[DAY_OF_MONTH] = measures[3].toInt()
                    this[MONTH] = measures[4].toInt()
                    this[YEAR] = measures[5].toInt()
                }
                catch (ex: Exception){}
            }
        }

	@Throws(IOException::class)
	fun stream2file(`in`: InputStream?): File? {
		val tempFile = File.createTempFile("text", ".txt")
		tempFile.deleteOnExit()
		FileOutputStream(tempFile).use { out -> IOUtils.copy(`in`, out) }
		return tempFile
	}
}

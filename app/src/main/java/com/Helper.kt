package com

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import org.apache.commons.io.IOUtils
import java.io.*


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
	
	@Throws(IOException::class)
	fun stream2file(`in`: InputStream?): File? {
		val tempFile = File.createTempFile("text", ".txt")
		tempFile.deleteOnExit()
		FileOutputStream(tempFile).use { out -> IOUtils.copy(`in`, out) }
		return tempFile
	}
}

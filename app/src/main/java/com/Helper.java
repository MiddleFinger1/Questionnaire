package com;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public abstract class Helper {

    public static String converting(InputStream inputStream){

        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ByteArrayOutputStream buf = new ByteArrayOutputStream();

            int result = bis.read();
            while(result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            return buf.toString();
        }
        catch (Exception ex){
            return "";
        }
    }
	
	//private static String getRealPathFromURI(Context context, Uri uri) {
	//	Cursor cursor = null;
	//	try {
	//		int[] proj = arrayOf(MediaStore.Images.Media.DATA);
	//		cursor = context.contentResolver.query(uri, proj, null, null, null);
	//		int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	//		cursor.moveToFirst();
	//		return cursor.getString(columnIndex);
	//	}
	//	catch (Exception ex){
	//		if (cursor != null) cursor.close();
	//		return "";
	//	}
	//
	//}
	
	/*
	private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        } finally {
            cursor?.close()
        }
    }
	*/
}

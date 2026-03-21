package com.example.rebuilt2026.database

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream

/**
 * Non-blocking function to fetch the database contents and save it to a csv file at the given
 * destination.
 */
fun saveDatabaseToCsv(
    uri: Uri?,
    database: TabletDatabase,
    contentResolver: ContentResolver
) {

    // Null check uri
    if (uri == null) {
        return
    }

    // Start a file IO coroutine scope
    CoroutineScope(Dispatchers.IO).launch {

        // Fetch database contents
        val matches = database.matchDataDao().all()

        try {

            // Create a buffered writer object to handle file IO
            contentResolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                FileOutputStream(descriptor.fileDescriptor).bufferedWriter().use { out ->

                    // Fetch property names
                    val names = MatchData.getPropertyNames()
                    // Join them to a string as headers
                    val headers = names.joinToString(separator = "") { "$it," } + "\n"
                    // Write property names as csv headers
                    out.write(headers)
                    println(headers)

                    // Write each MatchData instance as a row
                    matches.forEach { match ->
                        out.write(match
                            .getPropertyMap()
                            .map { prop -> prop.value }
                            .joinToString(separator = "") { value ->
                                // Check if value is a string, if so surround with quotes
                                if (value is String) {
                                    "\"${value.replace('\n', ';')}\","
                                } else {
                                    "$value,"
                                }
                            } + "\n"
                        )
                    }

                }
            }

        } catch (e: Exception) {
            // Only visible during debugging
            // Otherwise, we might just have to silently not export if an error occurs
            e.printStackTrace()
        }

    }

}
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class AvatarLetter {

    companion object {
        // Метод генерирует Bitmap и сохраняет его в File
        fun generateAsFile(context: Context, name: String, size: Int = 200): File {
            // 1. Создаем Bitmap с буквой
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val firstLetter = if (name.isNotEmpty()) name.substring(0, 1).uppercase(Locale.getDefault()) else "?"

            val colors = intArrayOf(Color.parseColor("#2196F3"), Color.parseColor("#4CAF50"), Color.parseColor("#FF9800"))
            val backgroundColor = colors[Math.abs(name.hashCode()) % colors.size]

            val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }
            val radius = size / 2f
            canvas.drawCircle(radius, radius, radius, circlePaint)

            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                textSize = size * 0.45f
                textAlign = Paint.Align.CENTER
                isFakeBoldText = true
            }
            val bounds = Rect()
            textPaint.getTextBounds(firstLetter, 0, firstLetter.length, bounds)
            val yCenter = radius + (bounds.height() / 2f)
            canvas.drawText(firstLetter, radius, yCenter, textPaint)

            // 2. Сохраняем в кэш и возвращаем File
            val file = File(context.cacheDir, "avatar_${name.hashCode()}.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            return file
        }
    }
}

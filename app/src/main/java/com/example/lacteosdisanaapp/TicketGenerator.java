package com.example.lacteosdisanaapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

import com.example.lacteosdisanaapp.Modelo.Pedido;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TicketGenerator {

    private Context context;

    public TicketGenerator(Context context) {
        this.context = context;
    }

    public void generatePDF(Pedido pedido, List<String> productos, String total) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Encabezado del ticket
        paint.setTextSize(16);
        canvas.drawText("Lácteos Disana", 80, 40, paint);
        paint.setTextSize(12);
        canvas.drawText("Ticket de Compra", 100, 60, paint);

        // Razón Social y RUC
        paint.setTextSize(12);
        canvas.drawText("Razón Social: AGROSERVISA CONSULTORES SAC", 10, 90, paint);
        canvas.drawText("RUC: 20529357096", 10, 110, paint);

        // Información del cliente
        canvas.drawText("Cliente: " + pedido.getCliente(), 10, 140, paint);

        // Formatear la fecha
        if (pedido.getFecha() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String fechaFormateada = dateFormat.format(pedido.getFecha().toDate());
            canvas.drawText("Fecha: " + fechaFormateada, 10, 160, paint);
        } else {
            canvas.drawText("Fecha: No disponible", 10, 160, paint);
        }

        // Detalles de los productos
        paint.setTextSize(12);
        canvas.drawText("Productos:", 10, 190, paint);
        int startY = 210;
        for (String producto : productos) {
            canvas.drawText(producto, 10, startY, paint);
            startY += 20;
        }

        // Total
        canvas.drawText("Total: S/ " + total, 10, startY + 20, paint);

        pdfDocument.finishPage(page);

        // Guardar el PDF en la carpeta de Descargas
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "ticket_" + System.currentTimeMillis() + ".pdf";
        File file = new File(directory, fileName);

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Ticket descargado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(context, "Error al generar el ticket", Toast.LENGTH_SHORT).show();
        } finally {
            pdfDocument.close();
        }
    }
}

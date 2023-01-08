package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sms.R;
import com.example.sms.model.Student;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentListReport extends AppCompatActivity {

    Button generate_studList;
    ImageView studentListReport_back;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Student> studentList;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_report);

        generate_studList = findViewById(R.id.generate_studList);
        studentListReport_back = findViewById(R.id.studentListReport_back);
        pdfView = findViewById(R.id.examResults_pdf_viewer);

        studentListReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        generate_studList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                studentList = new ArrayList<>();

                //create files in Report folder
                reportfile = new File("/storage/emulated/0/Report/");

                //check if they exist, if not create them(directory)
                if (!reportfile.exists()) {
                    reportfile.mkdirs();
                }
                rFile = new File(reportfile, "Students_List" + ".pdf");

                //fetch details;
                fetchStudentList();
                previewStudentsListReport(view);

            }
        });

    }

    public static String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static int PERMISSION_ALL = 12;

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createStudentListReport(List<Student> list) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");


        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(rFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 25, 20, 20});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        Chunk noText = new Chunk("\n" + "No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("\n" + "Student Name", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk subject = new Chunk("\n" + "Subject", white);
        PdfPCell selectedSubject = new PdfPCell(new Phrase(subject));
        selectedSubject.setFixedHeight(50);
        selectedSubject.setHorizontalAlignment(Element.ALIGN_CENTER);
        selectedSubject.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk grade = new Chunk("\n" + "Grade", white);
        PdfPCell gradesCell = new PdfPCell(new Phrase(grade));
        gradesCell.setFixedHeight(50);
        gradesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradesCell.setVerticalAlignment(Element.ALIGN_CENTER);

        count = studentList.size();
        Chunk footerText = new Chunk("\n\n" + "Total number of students is: " + count);
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(selectedSubject);
        table.addCell(gradesCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < list.size(); i++) {
            Student student = list.get(i);

            String id = String.valueOf(i + 1);
//          set the data from the list here
            String name = student.getUsername();
            String sub = student.getSubject();
            int studentGrade = student.getGrade();

            //IF

            table.addCell(id + ". ");
            table.addCell(name);
            table.addCell(sub + "");
            table.addCell(String.valueOf(studentGrade));

        }

        PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Students List\n\n", g));
        document.add(table);
        document.add(footTable);

        document.close();
    }

    //function to fetch data from the database
    private void fetchStudentList() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
//                    username.add(snapshot.getKey());
                    studentList.add(student);
                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createStudentListReport(studentList);
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }


    private void DisplayReport() {
        pdfView.fromFile(rFile)
//                .pages(0,2,1,3,3,3)
                .pages(0, 1, 2, 3, 4)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
    }

    public void previewStudentsListReport(View view) {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}
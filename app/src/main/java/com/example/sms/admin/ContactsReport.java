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

public class ContactsReport extends AppCompatActivity {


    ImageView contactsReport_back;
    Button generate_contactsList;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Student> studentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cotacts_report);

        generate_contactsList = findViewById(R.id.generate_contactsList);
        contactsReport_back = findViewById(R.id.contactsReport_back);
        pdfView = findViewById(R.id.contactsList_pdf_viewer);

        contactsReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        generate_contactsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                studentList = new ArrayList<>();

                //create files in Report folder
                reportfile = new File("/storage/emulated/0/Report/");

                //check if they exist, if not create them(directory)
                if ( !reportfile.exists()) {
                    reportfile.mkdirs();
                }
                rFile = new File(reportfile, "Contacts_List"+".pdf");

                //fetch details;
                fetchStudentContacts();
                previewContactsReport(view);

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

    private void createContactsListReport(List<Student> list) throws DocumentException, FileNotFoundException {
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

        Chunk noText = new Chunk("\n"+"No.", white);
        PdfPCell noCell = new PdfPCell(new Phrase(noText));
        noCell.setFixedHeight(50);
        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        noCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk nameText = new Chunk("\n"+"Student Name", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk phoneNo = new Chunk("\n"+"Phone Number", white);
        PdfPCell contactCell = new PdfPCell(new Phrase(phoneNo));
        contactCell.setFixedHeight(50);
        contactCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        contactCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk email = new Chunk("\n"+"Email ID", white);
        PdfPCell emailCell = new PdfPCell(new Phrase(email));
        emailCell.setFixedHeight(50);
        emailCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        emailCell.setVerticalAlignment(Element.ALIGN_CENTER);

        int count = studentList.size();
        Chunk footerText = new Chunk("\n\n"+"Total number of students is: "+count);
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(contactCell);
        table.addCell(emailCell);
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
            String contactNumber = student.getContact();
            String emailID = student.getEmail();

            //IF

            table.addCell(id + ". ");
            table.addCell(name);
            table.addCell(contactNumber+"");
            table.addCell(emailID);

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
    private void fetchStudentContacts() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student student = snapshot.getValue(Student.class);
                    studentList.add(student);
                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createContactsListReport(studentList);
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
                .pages(0,1,2,3,4)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();
    }

    public void previewContactsReport(View view) {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}
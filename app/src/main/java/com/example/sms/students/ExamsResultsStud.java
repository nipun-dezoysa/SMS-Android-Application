package com.example.sms.students;

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
import com.example.sms.model.Results;
import com.example.sms.model.Results1;
import com.example.sms.others.OnlineUsers;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class ExamsResultsStud extends AppCompatActivity {

    ImageView examresult_stud_back;
    Button generate_exam_report;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Results> mathsResultsList = new ArrayList<>();
    List<Results1> scienceResultsList;
    String uname;
    HashMap<String,Results> resultsHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_results_stud);

        generate_exam_report = findViewById(R.id.generate_exam_report);
        pdfView = findViewById(R.id.examResults_pdf_viewer);
        examresult_stud_back = findViewById(R.id.examresult_stud_back);

        examresult_stud_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Paper.init(ExamsResultsStud.this);
        uname = Paper.book().read(OnlineUsers.UserNamekey);

        examresult_stud_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        generate_exam_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                mathsResultsList = new ArrayList<>();
                scienceResultsList = new ArrayList<>();
                resultsHashMap = new HashMap<>();

                //create files in Report folder
                reportfile = new File("/storage/emulated/0/Report/");

                //check if they exist, if not create them(directory)
                if ( !reportfile.exists()) {
                    reportfile.mkdirs();
                }
                rFile = new File(reportfile, uname+"Results_List"+".pdf");

//                //fetch details;
//                fetchResultsList();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference1.child("students").child(uname).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String sub = snapshot.child("subject").getValue(String.class);
                        int g = snapshot.child("grade").getValue(int.class);
                        String grade = Integer.toString(g);

                        if (sub.equals("Maths Science")){
                            String subjectMaths = "Maths";
                            String subjectScience = "Science";
                            //fetch details;
                            fetchResultsList(subjectMaths, grade);
                            fetchResultsList(subjectScience, grade);
                        } else {
                            //fetch details;
                            fetchResultsList(sub,grade);
                        }
                        previewResultssListReport(view);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


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

    private void createStudentListReport(HashMap<String,Results> list) throws DocumentException, FileNotFoundException {
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

//        PdfPTable table1 = new PdfPTable(new float[]{6, 25, 20, 20});
//        table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//        table1.getDefaultCell().setFixedHeight(50);
//        table1.setWidthPercentage(100);
//        table1.setTotalWidth(PageSize.A4.getWidth());
//        table1.setWidthPercentage(100);
//        table1.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);


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

        Chunk subject = new Chunk("\n"+"Subject", white);
        PdfPCell selectedSubject = new PdfPCell(new Phrase(subject));
        selectedSubject.setFixedHeight(50);
        selectedSubject.setHorizontalAlignment(Element.ALIGN_CENTER);
        selectedSubject.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk grade = new Chunk("\n"+"Grade", white);
        PdfPCell gradesCell = new PdfPCell(new Phrase(grade));
        gradesCell.setFixedHeight(50);
        gradesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradesCell.setVerticalAlignment(Element.ALIGN_CENTER);

//        count = mathsResultsList.size();
        Chunk footerText = new Chunk("\n\n"+"Total number of students is: ");
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(4);
//
//        Chunk footerText1 = new Chunk("\n\n"+"Total number: ");
//        PdfPCell footCell1 = new PdfPCell(new Phrase(footerText1));
//        footCell1.setFixedHeight(70);
//        footCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//        footCell1.setVerticalAlignment(Element.ALIGN_CENTER);
//        footCell1.setColspan(4);


//        table1.addCell(String.valueOf(footerText1));
        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(selectedSubject);
        table.addCell(gradesCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        //IF


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
//        for (int i = 0; i < list.size(); i++) {
//            Results results = list.get(i);
////            Results1 results1 = list1.get(i);
//            String id = String.valueOf(i + 1);



            for (Map.Entry<String,Results> resultsEntry:resultsHashMap.entrySet()){
                String newSubject = resultsEntry.getKey();
                String username = resultsEntry.getValue().getUsername();
                String newGrade = resultsEntry.getValue().getGrades();


                table.addCell(1 + ". ");
                table.addCell(username);
                table.addCell(newSubject);
                table.addCell(newGrade);

            }

//            String id = String.valueOf(i + 1);
//          set the data from the list here
//            Float name = results.getTotal();
//            Float sub = results.getTotal();
//            String studentGrade = results.getGrades();

            //IF

//            table.addCell(id + ". ");
//            table.addCell(name+"");
//            table.addCell(sub+"");
//            table.addCell(String.valueOf(studentGrade));

//        }

        PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Students List\n\n", g));
        document.add(table);
//        document.add(table1);
        document.add(footTable);


        document.close();
    }

    //function to fetch data from the database
    private void fetchResultsList(String subject, String grade) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Results")
                .child("2023").child("Term 1").child(grade).child(subject);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (subject.equals("Maths")){
                        Results results = snapshot.getValue(Results.class);
                        if (results.getUsername().equals(uname)){
                            resultsHashMap.put("Maths", results);
                        }
                    }
                    if (subject.equals("Science")){
                        Results results = snapshot.getValue(Results.class);
                        if (results.getUsername().equals(uname)){
                            resultsHashMap.put("Science", results);
                        }
                    }

                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createStudentListReport(resultsHashMap);
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

    public void previewResultssListReport(View view) {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }


}
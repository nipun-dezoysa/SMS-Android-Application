package com.example.sms.students;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.admin.ExamsResultsReport;
import com.example.sms.admin.ViewAttendance;
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
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Paper;

public class ExamsResultsStud extends AppCompatActivity {

    ImageView examresult_stud_back;
    Button generate_exam_report;
    TextView term1_report_stud, term2_report_stud, term3_report_stud;
    String currentYear;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Results> mathsResultsList = new ArrayList<>();
    List<Results1> scienceResultsList;
    String uname, term;
    HashMap<String, Results> resultsHashMap;

    String username;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams_results_stud);

        generate_exam_report = findViewById(R.id.generate_exam_report);
        pdfView = findViewById(R.id.examResults_pdf_viewer);
        examresult_stud_back = findViewById(R.id.examresult_stud_back);
        term1_report_stud = findViewById(R.id.term1_report_stud);
        term2_report_stud = findViewById(R.id.term2_report_stud);
        term3_report_stud = findViewById(R.id.term3_report_stud);

        int intYear = Year.now().getValue();
        currentYear = intYear + "";

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

        term1_report_stud.setTextColor(getResources().getColor(R.color.white));
        term1_report_stud.setBackground(getDrawable(R.drawable.switch_trcks));
        term2_report_stud.setBackground(null);
        term2_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
        term3_report_stud.setBackground(null);
        term3_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
        term = "Term 1";

        term1_report_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term1_report_stud.setTextColor(getResources().getColor(R.color.white));
                term1_report_stud.setBackground(getDrawable(R.drawable.switch_trcks));
                term2_report_stud.setBackground(null);
                term2_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term3_report_stud.setBackground(null);
                term3_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 1";
            }
        });

        term2_report_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term2_report_stud.setTextColor(getResources().getColor(R.color.white));
                term2_report_stud.setBackground(getDrawable(R.drawable.switch_trcks));
                term1_report_stud.setBackground(null);
                term1_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term3_report_stud.setBackground(null);
                term3_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 2";
            }
        });

        term3_report_stud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term3_report_stud.setTextColor(getResources().getColor(R.color.white));
                term3_report_stud.setBackground(getDrawable(R.drawable.switch_trcks));
                term2_report_stud.setBackground(null);
                term2_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term1_report_stud.setBackground(null);
                term1_report_stud.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 3";
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
                if (!reportfile.exists()) {
                    reportfile.mkdirs();
                }
                rFile = new File(reportfile, uname + "Results_List" + ".pdf");

//                //fetch details;
//                fetchResultsList();
                DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
                databaseReference1.child("students").child(uname).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String sub = snapshot.child("subject").getValue(String.class);
                        int g = snapshot.child("grade").getValue(int.class);
                        String grade = Integer.toString(g);

                        if (sub.equals("Maths Science")) {
                            String subjectMaths = "Maths";
                            String subjectScience = "Science";
                            //fetch details;
                            fetchResultsList(subjectMaths, grade);
                            fetchResultsList(subjectScience, grade);
                        } else {
                            //fetch details;
                            fetchResultsList(sub, grade);
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

    private void createStudentListReport(HashMap<String, Results> list) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");


        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(rFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{8, 18, 18, 14, 14, 14, 14});
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

        Chunk nameText = new Chunk("\n" + "Student", white);
        PdfPCell nameCell = new PdfPCell(new Phrase(nameText));
        nameCell.setFixedHeight(50);
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        nameCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk subject = new Chunk("\n" + "Subject", white);
        PdfPCell selectedSubject = new PdfPCell(new Phrase(subject));
        selectedSubject.setFixedHeight(50);
        selectedSubject.setHorizontalAlignment(Element.ALIGN_CENTER);
        selectedSubject.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk part1 = new Chunk("\n" + "Part 1", white);
        PdfPCell part1Cell = new PdfPCell(new Phrase(part1));
        part1Cell.setFixedHeight(50);
        part1Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        part1Cell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk part2 = new Chunk("\n" + "Part 2", white);
        PdfPCell part2Cell = new PdfPCell(new Phrase(part2));
        part2Cell.setFixedHeight(50);
        part2Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        part2Cell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk total = new Chunk("\n" + "Total", white);
        PdfPCell totalCell = new PdfPCell(new Phrase(total));
        totalCell.setFixedHeight(50);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk grade = new Chunk("\n" + "Grade", white);
        PdfPCell gradesCell = new PdfPCell(new Phrase(grade));
        gradesCell.setFixedHeight(50);
        gradesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradesCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk footerText = new Chunk("");
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(7);

        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(selectedSubject);
        table.addCell(part1Cell);
        table.addCell(part2Cell);
        table.addCell(totalCell);
        table.addCell(gradesCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }


        int i = 0;

        for (Map.Entry<String, Results> resultsEntry : resultsHashMap.entrySet()) {
            String newSubject = resultsEntry.getKey();
            username = resultsEntry.getValue().getUsername();
            float part1Mark = resultsEntry.getValue().getPart1();
            float part2Mark = resultsEntry.getValue().getPart2();
            float totalMark = resultsEntry.getValue().getTotal();
            String newGrade = resultsEntry.getValue().getGrades();

            i = i + 1;

            table.addCell(i + ". ");
            table.addCell(username);
            table.addCell(newSubject);
            table.addCell(String.valueOf(part1Mark));
            table.addCell(String.valueOf(part2Mark));
            table.addCell(String.valueOf(totalMark));
            table.addCell(newGrade);

        }
        PdfPTable footTable = new PdfPTable(new float[]{8, 18, 18, 14, 14, 14, 14});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(term + " marks for " + username + "\n\n", g));
        document.add(table);
        document.add(footTable);


        document.close();
    }

    //function to fetch data from the database
    private void fetchResultsList(String subject, String grade) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Results")
                .child(currentYear).child(term).child(grade).child(subject);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (subject.equals("Maths")) {
                        Results results = snapshot.getValue(Results.class);
                        if (results.getUsername().equals(uname)) {
                            resultsHashMap.put("Maths", results);
                        }
                    }
                    if (subject.equals("Science")) {
                        Results results = snapshot.getValue(Results.class);
                        if (results.getUsername().equals(uname)) {
                            resultsHashMap.put("Science", results);
                        }
                    }

                }
                //create a pdf file and catch exception because file may not be created
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
                .pages(0, 1, 2, 3, 4)
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
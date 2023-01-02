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
import android.widget.TextView;

import com.example.sms.R;
import com.example.sms.model.Results;
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

public class ExamsResultsReport extends AppCompatActivity {

    ImageView examResultsReport_back;
    TextView grade10_list_report,grade11_list_report,maths_list_report,science_list_report,term1_report
            ,term2_report,term3_report;
    Button generate_results_button;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Results> results;

    String subject,studGrade,term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_results_report);

        examResultsReport_back = findViewById(R.id.examResultsReport_back);
        generate_results_button = findViewById(R.id.generate_results);
        grade10_list_report = findViewById(R.id.grade10_list_report);
        grade11_list_report = findViewById(R.id.grade11_list_report);
        maths_list_report = findViewById(R.id.maths_list_report);
        science_list_report = findViewById(R.id.science_list_report);
        term1_report = findViewById(R.id.term1_report);
        term2_report = findViewById(R.id.term2_report);
        term3_report = findViewById(R.id.term3_report);
        pdfView = findViewById(R.id.results_pdf_viewer);

        examResultsReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        grade10_list_report.setTextColor(getResources().getColor(R.color.white));
        grade10_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
        grade11_list_report.setBackground(null);
        grade11_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
        studGrade = "10";
        maths_list_report.setTextColor(getResources().getColor(R.color.white));
        maths_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
        science_list_report.setBackground(null);
        science_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
        subject = "Maths";
        term1_report.setTextColor(getResources().getColor(R.color.white));
        term1_report.setBackground(getDrawable(R.drawable.switch_trcks));
        term2_report.setBackground(null);
        term2_report.setTextColor(getResources().getColor(R.color.darkgreen));
        term3_report.setBackground(null);
        term3_report.setTextColor(getResources().getColor(R.color.darkgreen));
        term = "Term 1";

        term1_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term1_report.setTextColor(getResources().getColor(R.color.white));
                term1_report.setBackground(getDrawable(R.drawable.switch_trcks));
                term2_report.setBackground(null);
                term2_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term3_report.setBackground(null);
                term3_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 1";
            }
        });

        term2_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term2_report.setTextColor(getResources().getColor(R.color.white));
                term2_report.setBackground(getDrawable(R.drawable.switch_trcks));
                term1_report.setBackground(null);
                term1_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term3_report.setBackground(null);
                term3_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 2";
            }
        });

        term3_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                term3_report.setTextColor(getResources().getColor(R.color.white));
                term3_report.setBackground(getDrawable(R.drawable.switch_trcks));
                term1_report.setBackground(null);
                term1_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term2_report.setBackground(null);
                term2_report.setTextColor(getResources().getColor(R.color.darkgreen));
                term = "Term 3";
            }
        });

        grade10_list_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade10_list_report.setTextColor(getResources().getColor(R.color.white));
                grade10_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
                grade11_list_report.setBackground(null);
                grade11_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
                studGrade = "10";
//                maths_list_report.setTextColor(getResources().getColor(R.color.white));
//                maths_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
//                science_list_report.setBackground(null);
//                science_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
//                subject = "Maths";

            }
        });

        grade11_list_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade11_list_report.setTextColor(getResources().getColor(R.color.white));
                grade11_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
                grade10_list_report.setBackground(null);
                grade10_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
                studGrade = "11";
//                maths_list_report.setTextColor(getResources().getColor(R.color.white));
//                maths_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
//                science_list_report.setBackground(null);
//                science_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
//                subject = "Maths";
            }
        });

        maths_list_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maths_list_report.setTextColor(getResources().getColor(R.color.white));
                maths_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
                science_list_report.setBackground(null);
                science_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
                subject = "Maths";
            }
        });

        science_list_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                science_list_report.setTextColor(getResources().getColor(R.color.white));
                science_list_report.setBackground(getDrawable(R.drawable.switch_trcks));
                maths_list_report.setBackground(null);
                maths_list_report.setTextColor(getResources().getColor(R.color.darkgreen));
                subject = "Science";

            }
        });


        generate_results_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                results = new ArrayList<>();

                //create files in Report folder
                reportfile = new File("/storage/emulated/0/Report/");

                //check if they exist, if not create them(directory)
                if ( !reportfile.exists()) {
                    reportfile.mkdirs();
                }
                rFile = new File(reportfile, term+studGrade+subject+"Students_Exam_Results"+".pdf");

                //fetch details;
                fetchStudentResults(term,subject,studGrade);
                previewExamResultsReport(view);

            }
        });

//

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
    private void createResultsReport(List<Results> results) throws DocumentException, FileNotFoundException {
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

        Chunk totalMarks = new Chunk("\n"+"Total Marks", white);
        PdfPCell totalMarksCell = new PdfPCell(new Phrase(totalMarks));
        totalMarksCell.setFixedHeight(50);
        totalMarksCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalMarksCell.setVerticalAlignment(Element.ALIGN_CENTER);

        Chunk grades = new Chunk("\n"+"Grades", white);
        PdfPCell gradesCell = new PdfPCell(new Phrase(grades));
        gradesCell.setFixedHeight(50);
        gradesCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        gradesCell.setVerticalAlignment(Element.ALIGN_CENTER);

        int count = results.size();
        Chunk footerText = new Chunk("\n\n"+"Total number of students is: "+count);
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(totalMarksCell);
        table.addCell(gradesCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < results.size(); i++) {
            Results res = results.get(i);

            String id = String.valueOf(i + 1);
//            Todo : set the data from the results here
            String name = res.getUsername();
            Float marks = res.getTotal();
            String grade = res.getGrades();

            //IF

            table.addCell(id + ". ");
            table.addCell(name);
            table.addCell(marks+"");
            table.addCell(grade);

        }

        PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Term 1\n\n", g));
        document.add(table);
        document.add(footTable);

        document.close();
    }

    //function to fetch data from the database
    private void fetchStudentResults(String term, String subject, String studGrade)
    {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Results")
                .child("2023").child(term).child(studGrade).child(subject);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Results res = snapshot.getValue(Results.class);
                    results.add(res);
                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createResultsReport(results);
                } catch (DocumentException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }


    private void DisplayReport()
    {
        pdfView.fromFile(rFile)
//                .pages(0,2,1,3,3,3)
                .pages(0,1,2,3,4)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();


    }

    public void previewExamResultsReport(View view) {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}
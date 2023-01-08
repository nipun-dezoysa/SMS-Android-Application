package com.example.sms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sms.R;
import com.example.sms.model.Attendance;
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
import com.sdsmdg.tastytoast.TastyToast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AttendanceReport extends AppCompatActivity {

    Button generate_attendanceList;
    EditText date, month, year;
    ImageView attendanceListReport_back;
    TextView grade10_list_attendance, grade11_list_attendance;

    String grade;

    public static File rFile;
    private File reportfile;
    private PDFView pdfView;
    List<Attendance> attendanceList;
    List<Attendance> presentList = new ArrayList<>();
    List<Attendance> absentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        attendanceListReport_back = findViewById(R.id.attendanceListReport_back);
        generate_attendanceList = findViewById(R.id.generate_attendanceList);
        grade10_list_attendance = findViewById(R.id.grade10_list_attendance);
        grade11_list_attendance = findViewById(R.id.grade11_list_attendance);
        pdfView = findViewById(R.id.attendanceList_pdf_viewer);

        date = findViewById(R.id.date);
        month = findViewById(R.id.month);
        year = findViewById(R.id.year);

        grade10_list_attendance.setTextColor(getResources().getColor(R.color.white));
        grade10_list_attendance.setBackground(getDrawable(R.drawable.switch_trcks));
        grade11_list_attendance.setBackground(null);
        grade11_list_attendance.setTextColor(getResources().getColor(R.color.darkgreen));
        grade = "Grade 10";

        date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && s.toString().trim().equals("0")) {

                    date.getText().clear();
                }
                if (s.toString().trim().length() > 2 ){
                    TastyToast.makeText(AttendanceReport.this, "Please give a valid date", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    date.getText().clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                int ddate = Integer.parseInt(date.getText().toString());
//                if (ddate>31){
//                    TastyToast.makeText(AttendanceReport.this, "Please give a valid date ", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
//                }
            }
        });
        month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && s.toString().trim().equals("0")) {

                    month.getText().clear();
                }
                if (s.toString().trim().length() > 2 ){
                    TastyToast.makeText(AttendanceReport.this, "Please give a valid month", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    month.getText().clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        year.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && s.toString().trim().equals("0")) {

                    year.getText().clear();
                }
                if (s.toString().trim().length() > 4 ){
                    TastyToast.makeText(AttendanceReport.this, "Please give a valid month", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
                    year.getText().clear();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        grade10_list_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade10_list_attendance.setTextColor(getResources().getColor(R.color.white));
                grade10_list_attendance.setBackground(getDrawable(R.drawable.switch_trcks));
                grade11_list_attendance.setBackground(null);
                grade11_list_attendance.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = "Grade 10";
            }
        });

        grade11_list_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade11_list_attendance.setTextColor(getResources().getColor(R.color.white));
                grade11_list_attendance.setBackground(getDrawable(R.drawable.switch_trcks));
                grade10_list_attendance.setBackground(null);
                grade10_list_attendance.setTextColor(getResources().getColor(R.color.darkgreen));
                grade = "Grade 11";
            }
        });

        attendanceListReport_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        generate_attendanceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                attendanceList = new ArrayList<>();

                //create files in Report folder
                reportfile = new File("/storage/emulated/0/Report/");
                String dateTxt = date.getText().toString();
                String monthTxt = month.getText().toString();
                String yearTxt = year.getText().toString();
                //check if they exist, if not create them(directory)
                if (!reportfile.exists()) {
                    reportfile.mkdirs();
                }
//                rFile = new File(reportfile, grade+yearTxt+"/"+monthTxt+"/"+dateTxt+"Attendance_List"+".pdf");
                rFile = new File(reportfile, grade + yearTxt + monthTxt + dateTxt + "Attendance_List" + ".pdf");


                if (dateTxt.isEmpty() || monthTxt.isEmpty() || yearTxt.isEmpty()) {
                    TastyToast.makeText(AttendanceReport.this, "Please Enter a Date", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {
                    //fetch details;
                    fetchAttendanceList(dateTxt, monthTxt, yearTxt);
                    previewAttendanceListReport(view);
                }
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

    private void createStudentListReport(List<Attendance> list) throws DocumentException, FileNotFoundException {
        BaseColor colorWhite = WebColors.getRGBColor("#ffffff");
        BaseColor colorBlue = WebColors.getRGBColor("#056FAA");
        BaseColor grayColor = WebColors.getRGBColor("#425066");


        Font white = new Font(Font.FontFamily.HELVETICA, 15.0f, Font.BOLD, colorWhite);
        FileOutputStream output = new FileOutputStream(rFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{6, 25, 20});
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

        Chunk status = new Chunk("\n" + "Status", white);
        PdfPCell statusCell = new PdfPCell(new Phrase(status));
        statusCell.setFixedHeight(50);
        statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        statusCell.setVerticalAlignment(Element.ALIGN_CENTER);


        int presentCount = presentList.size();
        int absentCount = absentList.size();
        Chunk footerText = new Chunk("\n\n" + "Total number of Present is: " + presentCount + "\n\n" + "Total number of Absent is: " + absentCount);
        PdfPCell footCell = new PdfPCell(new Phrase(footerText));
        footCell.setFixedHeight(70);
        footCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        footCell.setVerticalAlignment(Element.ALIGN_CENTER);
        footCell.setColspan(4);


        table.addCell(noCell);
        table.addCell(nameCell);
        table.addCell(statusCell);
        table.setHeaderRows(1);

        PdfPCell[] cells = table.getRow(0).getCells();


        for (PdfPCell cell : cells) {
            cell.setBackgroundColor(grayColor);
        }
        for (int i = 0; i < list.size(); i++) {
            Attendance attendance = list.get(i);

            String id = String.valueOf(i + 1);
//          set the data from the list here
            String name = attendance.getUname();
            String status1 = attendance.getStatus();

            //IF

            table.addCell(id + ". ");
            table.addCell(name);
            table.addCell(status1);

        }

        PdfPTable footTable = new PdfPTable(new float[]{6, 25, 20, 20});
        footTable.setTotalWidth(PageSize.A4.getWidth());
        footTable.setWidthPercentage(100);
        footTable.addCell(footCell);

        PdfWriter.getInstance(document, output);
        document.open();
        Font g = new Font(Font.FontFamily.HELVETICA, 25.0f, Font.NORMAL, grayColor);
        document.add(new Paragraph(" Attendance List\n\n", g));
        document.add(table);
        document.add(footTable);

        document.close();
    }

    //function to fetch data from the database
    private void fetchAttendanceList(String date, String month, String year) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Attendance").child(year).child(month).child(date).child(grade);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                presentList.clear();
                absentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Attendance attendance = snapshot.getValue(Attendance.class);
                    attendanceList.add(attendance);
                    if (attendance.getStatus().equals("Present")) {
                        presentList.add(attendance);
                    } else if (attendance.getStatus().equals("Absent")) {
                        absentList.add(attendance);
                    }
                }
                //create a pdf file and catch exception beacause file may not be created
                try {
                    createStudentListReport(attendanceList);
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

    public void previewAttendanceListReport(View view) {
        if (hasPermissions(this, PERMISSIONS)) {
            DisplayReport();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }
}
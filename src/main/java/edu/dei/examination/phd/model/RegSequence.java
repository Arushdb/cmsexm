package edu.dei.examination.phd.model;


import javax.persistence.*;

@Entity
@Table(name = "reg_sequences",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"univ_code","program_code","year"})})
public class RegSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seqId;

    @Column(name = "univ_code", length = 10, nullable = false)
    private String univCode;

    @Column(name = "program_code", length = 20, nullable = false)
    private String programCode;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "last_serial", nullable = false)
    private Integer lastSerial;

    // constructors
    public RegSequence() {}

    public RegSequence(String univCode, String programCode, Integer year, Integer lastSerial) {
        this.univCode = univCode;
        this.programCode = programCode;
        this.year = year;
        this.lastSerial = lastSerial;
    }

    // getters & setters
    public Integer getSeqId() { return seqId; }
    public void setSeqId(Integer seqId) { this.seqId = seqId; }

    public String getUnivCode() { return univCode; }
    public void setUnivCode(String univCode) { this.univCode = univCode; }

    public String getProgramCode() { return programCode; }
    public void setProgramCode(String programCode) { this.programCode = programCode; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public Integer getLastSerial() { return lastSerial; }
    public void setLastSerial(Integer lastSerial) { this.lastSerial = lastSerial; }
}

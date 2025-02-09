package nextstep.courses.domain.session;

import nextstep.users.domain.NsUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Students {
    private final int capacity;

    private SessionFeeType sessionFeeType;

    private SessionStatus sessionStatus;

    private SessionRecruitment sessionRecruitment;

    private final List<Student> users;

    public Students(int capacity, SessionFeeType sessionFeeType, SessionStatus sessionStatus) {
        this(capacity, sessionFeeType, sessionStatus, SessionRecruitment.CLOSE, new ArrayList<>());
    }

    public Students(int capacity, SessionFeeType sessionFeeType, SessionStatus sessionStatus, SessionRecruitment sessionRecruitment) {
        this(capacity, sessionFeeType, sessionStatus, sessionRecruitment, new ArrayList<>());
    }

    public Students(
            int capacity,
            SessionFeeType sessionFeeType,
            SessionStatus sessionStatus,
            SessionRecruitment sessionRecruitment,
            List<Student> users) {
        validateCapacity(capacity);
        Objects.requireNonNull(sessionFeeType);
        Objects.requireNonNull(sessionStatus);
        Objects.requireNonNull(sessionRecruitment);
        this.capacity = capacity;
        this.sessionFeeType = sessionFeeType;
        this.sessionStatus = sessionStatus;
        this.sessionRecruitment = sessionRecruitment;
        this.users = users;
    }

    private void validateCapacity(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("강의 최대 수강인원은 1보다 커야 합니다.");
        }
    }

    public void initUsers(List<Student> students) {
        users.addAll(students);
    }

    public Optional<Student> findStudentByUserId(Long userId) {
        return users.stream()
                .filter(user -> user.getUserId().equals(userId))
                .findAny();
    }

    public void validateRegister(NsUser nsUser) {
        if (sessionRecruitment.isClosed()) {
            throw new IllegalStateException("강의 수강신청은 강의 상태가 모집중일 때만 가능합니다.");
        }
        if (users.size() >= capacity) {
            throw new IllegalStateException("강의는 강의 최대 수강 인원을 초과할 수 없습니다.");
        }
        if (findStudentByUserId(nsUser.getId()).isPresent()) {
            throw new IllegalStateException("이미 등록된 학생입니다.");
        }
    }

    public Student validateExist(NsUser nsUser) {
        Optional<Student> student = findStudentByUserId(nsUser.getId());
        if (student.isEmpty()) {
            throw new IllegalArgumentException("수강 신청한 사용자가 아닙니다.");
        }
        return student.get();
    }

    public void startRecruit() {
        this.sessionRecruitment = SessionRecruitment.OPEN;
    }

    public int getCapacity() {
        return capacity;
    }

    public SessionFeeType getSessionFeeType() {
        return sessionFeeType;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public SessionRecruitment getSessionRecruitment() {
        return sessionRecruitment;
    }

    public List<Student> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        return "Students{" +
                "capacity=" + capacity +
                ", sessionFeeType=" + sessionFeeType +
                ", sessionStatus=" + sessionStatus +
                ", sessionRecruitment=" + sessionRecruitment +
                ", users=" + users +
                '}';
    }
}

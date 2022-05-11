package test;

import manager.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicStatusTest {    /* Расчёт статуса Epic
                           тестируем void changeEpicStatus(Epic epic)*/

    InMemoryTaskManager inMemoryTaskManager;
    Epic epic;
    Subtask subtask;
    Subtask subtask1;
    Subtask subtask2;
    String startTime;
    String startTime1;
    String startTime2;
    String startTime3;
    int duration;

    @BeforeEach
    public void beforeEach() {
        startTime = "2000-01-01T01:00:00.000000000";
        startTime1 = "2000-02-01T01:00:00.000000000";
        startTime2 = "2001-01-01T01:00:00.000000000";
        startTime3 = "2002-01-01T01:00:00.000000000";
        duration = 3;
        inMemoryTaskManager = new InMemoryTaskManager();
        epic = new Epic();
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        subtask = new Subtask();
        subtask1 = new Subtask();
        subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask, startTime1, duration);
        inMemoryTaskManager.createSubTask(epic, subtask1, startTime2, duration);
        inMemoryTaskManager.createSubTask(epic, subtask2, startTime3, duration);
    }

    @Test
    public void shouldReturnNEWwhenAllSubtaskNEW() {
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnDONEwhenAllSubtaskDONE(){
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldReturnINPROGRESSwhenSubtaskDONEandNEW() {
        subtask.setStatus(Status.NEW);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnINPROGRESSwhenSubtaskIN_PROGRESS(){
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnINPROGRESSwhenSubtaskIN_PROGRESSandDONEandNEW(){
        subtask.setStatus(Status.NEW);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}

class EpicStatusTestWithNoSubtasks {    /* Расчёт статуса Epic
                                           тестируем void changeEpicStatus(Epic epic) без подзадач*/

    @Test
    public void shouldReturnNEWwhenNoSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        String startTime = "2000-01-01T01:00:00.000000000";
        int duration = 3;
        inMemoryTaskManager.createEpic(epic, startTime, duration);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

}
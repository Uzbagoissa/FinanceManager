package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicStatusTest {    /* Расчёт статуса Epic
                           тестируем void changeEpicStatus(Epic epic)*/
    @Test
    public void shouldReturnNEWwhenNoSubtask() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnNEWwhenAllSubtaskNEW() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    public void shouldReturnDONEwhenAllSubtaskDONE(){
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        subtask.setStatus(Status.DONE);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    public void shouldReturnIN_PROGRESSwhenSubtaskDONEandNEW() {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        subtask.setStatus(Status.NEW);
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.NEW);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnIN_PROGRESSwhenSubtaskIN_PROGRESS(){
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    public void shouldReturnIN_PROGRESSwhenSubtaskIN_PROGRESSandDONEandNEW(){
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask();
        Subtask subtask1 = new Subtask();
        Subtask subtask2 = new Subtask();
        inMemoryTaskManager.createSubTask(epic, subtask);
        inMemoryTaskManager.createSubTask(epic, subtask1);
        inMemoryTaskManager.createSubTask(epic, subtask2);
        subtask.setStatus(Status.IN_PROGRESS);
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        inMemoryTaskManager.changeEpicStatus(epic);
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

}
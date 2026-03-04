public class Managers {
    static HistoryManager sharedHistory = new InMemoryHistoryManager();

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(sharedHistory);
    }

    public static HistoryManager getDefaultHistory() {
        return sharedHistory;
    }
}

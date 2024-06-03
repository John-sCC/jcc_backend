package com.nighthawk.spring_portfolio.mvc.classPeriod;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

@RestController
public class UndoRedo {
    private Stack<ActionData> undoStack;
    private Stack<ActionData> redoStack;
    private Queue<ActionData> actionsQueue;

    public UndoRedo() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        actionsQueue = new LinkedList<>();
    }

    @PostMapping("/addAction")
    public String addAction(@RequestBody ActionData actionData) {
        undoStack.push(actionData);
        redoStack.clear(); // Clear redo stack on new action
        actionsQueue.add(actionData);
        return "Action added: " + actionData.getType(); // Example: Assuming ActionData has a getType() method
    }

    @GetMapping("/undo")
    public String undo() {
        if (!undoStack.isEmpty()) {
            ActionData actionData = undoStack.pop();
            redoStack.push(actionData);
            return "Undo action: " + actionData.getType();
        }
        return "No actions to undo";
    }

    @GetMapping("/redo")
    public String redo() {
        if (!redoStack.isEmpty()) {
            ActionData actionData = redoStack.pop();
            undoStack.push(actionData);
            return "Redo action: " + actionData.getType();
        }
        return "No actions to redo";
    }

    @GetMapping("/getActionsQueue")
    public Queue<ActionData> getActionsQueue() {
        return actionsQueue;
    }
}

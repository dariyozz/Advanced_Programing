package v1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class NoSuchUserException extends Exception {
    public NoSuchUserException(String username) {
        super("No such user: " + username);
    }
}

class NoSuchRoomException extends Exception {
    public NoSuchRoomException(String roomName) {
        super("No such room: " + roomName);
    }
}


class ChatRoom {
    private String name;                // Име на собата
    private Set<String> users;          // Сет со кориснички имиња, алфабетски подредени

    public ChatRoom(String name) {
        this.name = name;
        this.users = new TreeSet<>();
    }

    public void addUser(String username) {
        users.add(username);
    }

    public void removeUser(String username) {
        users.remove(username);
    }

    public boolean hasUser(String username) {
        return users.contains(username);
    }

    public int numUsers() {
        return users.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name); // Append only the room name without a new line yet

        if (users.isEmpty()) {
            sb.append("\nEMPTY");
        } else {
            users.stream()
                    .sorted()
                    .forEach(user -> sb.append("\n").append(user)); // Add new line before each user
        }

        return sb.append("\n").toString();
    }


    public String getName() {
        return name;
    }
}

class ChatSystem {
    private Map<String, ChatRoom> rooms;
    private Set<String> users;

    public ChatSystem() {
        this.rooms = new TreeMap<>();
        this.users = new TreeSet<>();
    }

    public void addRoom(String roomName) {
        rooms.put(roomName, new ChatRoom(roomName));
    }

    public void removeRoom(String roomName) {
        rooms.remove(roomName);
    }

    public ChatRoom getRoom(String roomName) throws NoSuchRoomException {
        if (!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        return rooms.get(roomName);
    }

    public void register(String userName) {
        users.add(userName);
        rooms.values()
                .stream()
                .min(Comparator.comparingInt(ChatRoom::numUsers)
                        .thenComparing(ChatRoom::getName)).ifPresent(minRoom -> minRoom.addUser(userName));

    }

    public void registerAndJoin(String userName, String roomName) throws NoSuchRoomException {
        users.add(userName);
        if (!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        rooms.get(roomName).addUser(userName);
    }

    public void joinRoom(String userName, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(userName)) {
            throw new NoSuchUserException(userName);
        }
        if (!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        rooms.get(roomName).addUser(userName);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException {
        if (!users.contains(username)) {
            throw new NoSuchUserException(username);
        }
        if (!rooms.containsKey(roomName)) {
            throw new NoSuchRoomException(roomName);
        }
        rooms.get(roomName).removeUser(username);
    }

    public void followFriend(String username, String friendUsername) throws NoSuchUserException {
        if (!users.contains(username) || !users.contains(friendUsername)) {
            throw new NoSuchUserException(username);
        }
        for (ChatRoom room : rooms.values()) {
            if (room.hasUser(friendUsername)) {
                room.addUser(username);
            }
        }
    }
}

public class ChatSystemTest {

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        boolean has = false;
        if (k == 0) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr.addUser(jin.next());
                if (k == 1) cr.removeUser(jin.next());
                if (k == 2) {
                    System.out.println(cr.hasUser(jin.next()));
                    has = true;
                }
            }
            if (has)
                System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if (n == 0) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for (int i = 0; i < n; ++i) {
                k = jin.nextInt();
                if (k == 0) cr2.addUser(jin.next());
                if (k == 1) cr2.removeUser(jin.next());
                if (k == 2) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if (k == 1) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while (true) {
                String cmd = jin.next();
                if (cmd.equals("stop")) break;
                if (cmd.equals("print")) {
                    System.out.println(cs.getRoom(jin.next()) + "\n");
                    continue;
                }

                for (Method m : mts) {
                    if (m.getName().equals(cmd)) {
                        Class<?>[] paramTypes = m.getParameterTypes();
                        Object[] params = new Object[paramTypes.length];

                        for (int i = 0; i < paramTypes.length; i++) {
                            if (paramTypes[i] == String.class) {
                                params[i] = jin.next();
                            }
                        }
                        m.invoke(cs, params);
                    }
                }
            }
        }
    }

}

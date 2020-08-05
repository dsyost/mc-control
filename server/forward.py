import subprocess
import shlex
import threading
from io import StringIO
import socket

class MinecraftServer:

    def __init__(self, writePlace):
        self.writePlace = writePlace
        self.thread = threading.Thread(target=self.read,daemon=True)
        self.restart()

    def restart(self):
        if self.thread.is_alive():
            self.thread.join()
        self.process = subprocess.Popen(shlex.split("java -Xmx1024M -Xms1024M -jar server.jar nogui"), stdout=subprocess.PIPE, stdin=subprocess.PIPE)
        self.thread = threading.Thread(target=self.read,daemon=True)
        self.thread.start()
        self.loaded = False

    def read(self):
        while True:
            output = self.process.stdout.readline().decode().strip()
            if output == "" and self.process.poll is not None:
                break
            if output:
                self.push(output)
                if output[-5:-1] == "help":
                    self.loaded = True
        rc = self.process.poll()
        return rc

    def push(self, msg):
        self.writePlace(msg+"\n")

    def write(self, msg):
        self.process.stdin.write((msg).encode())
        self.process.stdin.flush()

class SocketServer:

    def __init__(self):
        self.serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.serversocket.bind((socket.gethostname(), 25564))
        self.serversocket.listen(5)
        self.thread = threading.Thread(target=self.listenLoop, daemon=True)
        self.thread.start()
        self.clients = []
        self.cleanClients = threading.Thread(target=self.cleanThreads, daemon=True)
        self.cleanClients.start()
        self.writePlace = ("", False)
        
    def cleanThreads(self):
        while True:
            for (thread,client) in self.clients:
                if client.fileno() == -1:
                    self.clients.remove((thread,client))
                    thread.join()

    def setWritePlace(self, place):
        self.writePlace = (place, True)

    def listenLoop(self):
        while True:
            (clientsocket, address) = self.serversocket.accept()
            t = threading.Thread(target=self.clientListen, args=(clientsocket,), daemon=True)
            t.start()
            self.clients.append((t,clientsocket))

    def clientListen(self,client):
        while True:
            msg = client.recv(1024).decode()
            if msg.encode() == b'close\r\n':
                client.close()
                break
            else:
                self.write(msg)

    def write(self, msg):
        if self.writePlace[1]:
            self.writePlace[0](msg)

    def send(self, msg):
        for (thread,client) in self.clients:
            if client.fileno() != -1:
                client.send(msg.encode())

if __name__ == "__main__":
    s = SocketServer()
    m = MinecraftServer(s.send)
    s.setWritePlace(m.write)

    while True:
        while m.thread.is_alive():
            pass
        m.restart()
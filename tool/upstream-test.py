import socket
import sys

from multiprocessing import Process

def handle_client(client_socket) :
    request = client_socket.recv(1024)
    response_first_line = "HTTP/1.1 200 OK\r\n"
    response_headers = "Server: Python Upstream Test\r\n"
    response_body = "Port " + sys.argv[1]
    response = response_first_line + response_headers + "\r\n" + response_body
    client_socket.send(bytes(response, "utf-8"))
    client_socket.close()

if __name__ == "__main__":
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(("", int(sys.argv[1])))
    server_socket.listen(128)

    while True:
        client_socket, client_address = server_socket.accept()
        handle_client_process = Process(target=handle_client, args=(client_socket,))
        handle_client_process.start()
        client_socket.close()
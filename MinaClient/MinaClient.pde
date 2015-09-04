import processing.net.*;
import java.util.*;

Client socket;
color col[] = {#FF0000, #00FF00, #0000FF};
int th = 0;

void setup() 
{
  size(300, 300);
  noStroke();
  socket = new Client(this, "localhost", 9123);
  socket.write("hello\n");
}

void keyPressed() 
{
  socket.write("quit\n");
  socket.stop();  
  exit();
}

void draw() 
{
  if (socket.available() == 0) return;

  String[] item = split(socket.readString(), ',');
  th = (th + 3) % 360;
  socket.write(item[0] + "," + mouseX + "," + mouseY + "," + th + "\n");
  if (item.length < 6) return;
  
  //item[0] : sessionid
  //item[1] : player num
  //item[2] : player id
  //item[3] : x
  //item[4] : y
  //item[5] : th
  
  background(0);
  text("push any key to quit", 0, 15);
  int num = int(item[1]);
  if ((item.length - 2) != num * 4) return;
  for (int i = 0; i < num; i++) {
    fill(col[i]);
    pushMatrix();
    translate(int(item[i * 4 + 1 + 2]), int(item[i * 4 + 2 + 2]));
    rotate(int(trim(item[i * 4 + 3 + 2])) / 180.0 * PI);
    triangle(0, 30, -15, -30, 15, -30);
    popMatrix();
  }
}


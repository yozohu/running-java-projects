import os
import subprocess
from pprint import pprint
import time

from os.path import relpath
from subprocess import STDOUT,PIPE

def test_graph(project_root_directory, command_file, graph_file, student_id="yusra"):
    
    # get commands ready
    cmd_file = open(command_file, "r")
    cmd_contents = cmd_file.readlines()
    cmd_file.close()

    # to look at the actual code
    print("\nAdjacencyMatrix Code At: " + os.path.join(project_root_directory, "net", "datastructures", "AdjacencyMatrixGraph.java"))

    # create output file
    graph_output = os.path.join(project_root_directory, "graph_output_"+ student_id +".txt")
    f = open(graph_output, "w+")
    f.close()

    # delete class files
    for root, dirs, files in os.walk(project_root_directory):
        for name in files:
            if (".class" in name):
                os.remove(os.path.join(root, name)) 
 
    # compile
    print("\nCompiling the Java project ...")
    os.system(str.format("cd {} && javac *.java", project_root_directory))
    
    # run their Graph using graph2.txt
    java_cp = project_root_directory
    cmd = ['java', '-cp', java_cp, 'GraphTest', graph_file]
    
    # pipe in commands
    cmdIx = 0
    print("\nFeeding in commands ...")
    with open(graph_output,"w+") as stdout_file:
        try:
            with subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=stdout_file, stderr=STDOUT) as proc:
                while cmdIx < len(cmd_contents):
                    proc.stdin.write(cmd_contents[cmdIx].encode())
                    time.sleep(0.5) # allow input to be accounted for
                    proc.stdin.flush()
                    cmdIx = cmdIx +1
        except OSError:
            print("code crashed :(")


    # add commands back to file, for easy reading
    outfile = open(graph_output, "r")
    contents = outfile.readlines()
    outfile.close()

    cmdIx = 0
    for  n, line in enumerate(contents):
        if  "Enter command:" in line:
            contents[n] = contents[n].replace("Enter command: ", str.format("Enter command: {}\n", cmd_contents[cmdIx]) )
            cmdIx = cmdIx + 1
            if cmdIx >= len(cmd_contents):
                break
    

    f = open(graph_output, "w")
    f.writelines(contents)
    f.close()
    
    print("graph results here: " + graph_output)

# globals 
graph2_filepath = None #"C:/Users/yozoh/Documents/CSI2110_TA/graph2.txt"
command_filepath = None # "C:/Users/yozoh/Documents/CSI2110_TA/commands.txt"

# main
done = 'n'
while done != 'q':
    while True:
        a4_rootdir = input("Enter the root directory: ").strip('\"')
        if os.path.exists(a4_rootdir):
            a4_rootdir = os.path.normpath(a4_rootdir)
            break
        else:
            print("invalid path")
    
    if not graph2_filepath:
        while True:
            graph2_filepath  = input("Enter path to graph descriptor file (i.e. graph.txt): ").strip('\"')
            if os.path.exists(graph2_filepath):
                break
            else:
                print("invalid path")
    
    if not command_filepath:
        while True:
            command_filepath= input("Enter path to commands file (i.e.commands.txt): ").strip('\"')
            if os.path.exists(command_filepath):
                break
            else:
                print("invalid path")
    test_graph(project_root_directory=a4_rootdir, command_file=command_filepath, graph_file=graph2_filepath)
    done = input("\nHit enter to run again, or 'q' to quit program. ")
    
    if done !='q':
        choice = input("Reuse graph/command file? y/n: ")
        if choice == 'n':
            command_filepath = None
            graph2_filepath = None
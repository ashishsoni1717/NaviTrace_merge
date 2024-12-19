[Setup]
AppName=Traccar
AppVersion=6.5
DefaultDirName={pf}\Traccar
OutputBaseFilename=traccar-setup
ArchitecturesInstallIn64BitMode=x64

[Dirs]
Name: "{app}\data"
Name: "{app}\logs"

[Files]
Source: "out\*"; DestDir: "{app}"; Flags: recursesubdirs

[Run]
Filename: "{app}\jre\bin\java.exe"; Parameters: "-jar ""{app}\navitrace-server.jar"" --install .\conf\traccar.xml"; Flags: runhidden

[UninstallRun]
Filename: "{app}\jre\bin\java.exe"; Parameters: "-jar ""{app}\navitrace-server.jar"" --uninstall"; Flags: runhidden

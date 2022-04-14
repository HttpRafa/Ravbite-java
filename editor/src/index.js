const { app, BrowserWindow } = require('electron');
const path = require('path');

const iconPath = path.join(__dirname, '/assets/images/icon600.png');

if (require('electron-squirrel-startup')) {
  app.quit();
}

app.on('ready', event => {
  console.log("Starting the Ravbite Editor...");

  startEditor();
});

app.on('window-all-closed', () => {
  app.quit();
});

let startWindow;

function startEditor() {
    startWindow = new BrowserWindow({
      webPreferences: {
        nodeIntegration: true,
        contextIsolation: false
      },
      frame: false,
      transparent: true,
      autoHideMenuBar: true,
      skipTaskbar: false,
      title: 'Ravbite Editor',
      width: 650,
      height: 250,
      maxWidth: 650,
      minWidth: 650,
      maxHeight: 250,
      minHeight: 250,
      show: false,
      center: true,
      icon: iconPath
    });

    startWindow.loadFile(path.join(__dirname, '/startWindow.html')).then(r => {});
    startWindow.once("ready-to-show", function () {
      startWindow.show();
    });
}

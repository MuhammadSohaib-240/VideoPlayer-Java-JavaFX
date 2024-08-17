# Video Player - JavaFX, OpenCV & FFMPEG

## Description

This project is a feature-rich video player built using Java, JavaFX, OpenCV, and FFMPEG. It was my first major project, and I proudly participated in a project exhibition with it, where it won awards. The video player showcases my problem-solving skills and expertise in software design and architecture. It allows users to perform various video manipulation tasks and manage a video library with ease. The project emphasizes clean code, maintainability, and performance.

## Features

- **User-Friendly Interface**: A simple and intuitive UI built with JavaFX, designed for ease of use.
- **Video Manipulation**: Perform tasks like extracting frames, taking screenshots, and converting videos from RGB to monochromatic using FFMPEG commands.
- **Library Management**: Users can upload, delete, and update the names of videos in the library, with data management handled by SQLite3.
- **Cross-Platform Compatibility**: Runs smoothly on any system that supports Java, ensuring wide reach and usability.

## Setup and Installation

### Prerequisites

Before running the project, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 8 or later.
- **JavaFX**: The project is built using JavaFX, and the necessary dependencies are already included via Maven.
- **OpenCV (Java)**: Managed through Maven, so no manual setup is required.
- **FFMPEG**: You must download and install FFMPEG separately from the official website and add it to your system's PATH.

### FFMPEG Setup Instructions

1. **Download FFMPEG**:
   - Visit the [FFMPEG official website](https://ffmpeg.org/download.html) and download the latest stable version suitable for your operating system.

2. **Install and Add to PATH**:
   - Follow the installation instructions provided on the FFMPEG website.
   - Ensure the FFMPEG executable is added to your system's PATH so that it can be accessed directly from the command line.

### Maven Dependencies

The necessary libraries, including JavaFX, OpenCV, and SQLite3, are managed through Maven. The `pom.xml` file in the project already includes these dependencies, so there's no need for manual setup.

### Running the Project

After setting up the prerequisites, you can run the project through your IDE by executing the main class. The application should launch, allowing you to start managing and playing videos.

## Usage

1. Open the application.
2. Upload videos to the library.
3. Play a video and use the provided tools to:
   - Extract frames.
   - Take screenshots.
   - Convert videos from RGB to monochromatic.
4. Manage your video library by updating video names or deleting videos.
5. Save any changes or outputs to your desired location.

## Technologies Used

- **Java**: Core programming language.
- **JavaFX**: Used for building the graphical user interface.
- **OpenCV**: Used for video processing tasks.
- **FFMPEG**: Used for video manipulation via command-line operations.
- **SQLite3**: Used for managing the video library data.

## Acknowledgments

- Thanks to the OpenCV community for providing robust tools for computer vision tasks.
- Special thanks to the Java, JavaFX, and FFMPEG communities for extensive documentation and support.

## Contact

For any questions or further information, please feel free to contact me through my [GitHub profile](https://github.com/MuhammadSohaib-240).

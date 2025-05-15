# Handschriftenportal - bom remove plugin 

## Description

This repository contains the Maven-Plugin used by the TEI-ODD build process for the Handschriftenportal. 
This tool is independent of the backend services of the manuscripts portal "Handschriftenportal".
It's provide maven plugin for cleanup of the "Byte Order Mark (BOM)" from the text files.

## Technology stack

- The pipeline is implemented as a [Maven](https://maven.apache.org/) project.
- To build the project `mvn clean package` is used.

## Status

Beta (in development)

## Getting started

1. Get the source code

   ```
   git clone https://github.com/handschriftenportal-dev/hsp-bom-remove-plugin
   ```

2. Build the project

   ```
   mvn clean package
   ```


After a successful build, you will find the bom-remove-plugin jar  files in the `target` directory. In case of errors in the XML files, the pipeline may have stopped.

## Usage

The plugin can be used in the maven build process

```xml

<plugins>
  <plugin>
    <groupId>staatsbibliothek.berlin.maven</groupId>
    <artifactId>bom-remove-plugin</artifactId>
    <version>1.0.11</version>
    <configuration>
      <source>${process.all.files.from.this.directory}</source>
      <destination>${store.all.processed.files.to.that.directory}</destination>
      <recursive>true</recursive>
      <ignoreErrors>true</ignoreErrors>
    </configuration>
    <executions>
      <execution>
        <phase>validate</phase>
        <goals>
          <goal>BomRemove</goal>
        </goals>
      </execution>
    </executions>
  </plugin>
</plugins>
```

## Known issues

## Getting help

To get help please use our contact possibilities on [twitter](https://twitter.com/hsprtl)
and [handschriftenportal.de](https://handschriftenportal.de/)

## Getting involved

To get involved please contact our develoment
team [handschriftenportal@sbb.spk-berlin.de](handschriftenportal-dev@sbb.spk-berlin.de)

## Open source licensing info

The project is published under the [MIT License](https://opensource.org/licenses/MIT).

## Credits and references

1. [Github Project Repository](https://github.com/handschriftenportal-dev)
2. [Project Page](https://handschriftenportal.de/)
3. [Internal Documentation](docs/README.md)

## RELEASE Notes

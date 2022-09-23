# A tool for reference-free phylogeny

In this repository, you might find source codes for the reference-free phylogeny
presented in the paper:
Petr Ryšavý, Filip Železný
Reference-Free Phylogeny from Sequencing Data
TODO: we will provide a complete citation when the paper gets published

Aim of this tool is to calculate distance matrix between several genomes knowing
only raw read data and/or contig assemblies. The method is based on extension of
the Monge-Elkan distance.

To run the method yourself, download the relase bundle from [the relase](https://github.com/petrrysavy/reference-free-phylogeny/releases/tag/v2.0),
have Java installed on your system (source codes were compiled using Java 11),
and run on the attached simple dataset using commad
```bash
java -cp reference-free-phylogeny.jar rysavpe1.reads.cli.Main -c testdata/contigs/* -r testdata/reads/* -o distanceMatrix.dat -d 3
```
The distance matrix is then printed to the `distanceMatrix.dat` file.

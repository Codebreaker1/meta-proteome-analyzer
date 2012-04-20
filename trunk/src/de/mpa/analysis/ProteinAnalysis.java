package de.mpa.analysis;

import java.util.List;
import java.util.Map;

import de.mpa.algorithms.quantification.QuantMethod;
import de.mpa.client.model.dbsearch.PeptideHit;
import de.mpa.client.model.dbsearch.ProteinHit;
import de.mpa.util.Formatter;

/**
 * Helper class containing various protein-specific calculations.
 */
public class ProteinAnalysis {
	
	/**
	 * Calculates the molecular weight of a protein.
	 * @param The protein hit whose weight shall be calculated.
	 */
	public static double calculateMolecularWeight(ProteinHit proteinHit) {
		// Get the masses with the amino acid masses.
		Map<String, Double> masses = Masses.getInstance();
		
		// Start with the N-terminal mass
		double molWeight = Masses.N_term;
		
		// Get the protein sequence.
		String sequence = proteinHit.getSequence();
		
		// Iterate the protein sequence and add the molecular masses.
		for (char letter : sequence.toCharArray()) {
			// Skip the wildcard amino acid.
			if (letter != '*') {
				molWeight += masses.get(String.valueOf(letter));
			}
		}
	    
	    // Add the C-terminal mass.
	    molWeight += Masses.C_term;
	    
	    // Get the weight in kDa
	    molWeight = Formatter.roundDouble((molWeight / 1000.0), 3);
	   
	    return molWeight;
	}
	
	/**
	 * Calculates the sequence coverage of a protein hit with respect to its containing peptides. 
	 * Multiple occurences of a single peptide will be counted as a single occurence.
	 * @param proteinHit The protein hit whose coverage shall be calculated.
	 */
	public static double calculateSequenceCoverage(ProteinHit proteinHit) {
		return calculateSequenceCoverage(proteinHit, true);
	}
	
	/**
	 * Calculates the sequence coverage of a protein hit with respect to its containing peptides.
	 * @param proteinHit The protein hit whose coverage shall be calculated.
	 * @param hitsCoveredOnlyOnce Flag determining whether peptides are counted only once in a protein with repeats.
	 */
	public static double calculateSequenceCoverage(ProteinHit proteinHit, boolean hitsCoveredOnlyOnce) {
		//TODO problem sequence coverage of proteins with PTMs is 0
		// The Protein sequence.
		String sequence = proteinHit.getSequence();
		boolean[] foundAA = new boolean[sequence.length()];
		List<PeptideHit> peptides = proteinHit.getPeptideHitList();

		// Iterate the peptides in the protein.
		for (PeptideHit peptideHit : peptides) {
			// Indices for the pattern
			int startIndex = 0;
			int endIndex = 0;
			// The pattern == The peptide sequence
			String pattern = peptideHit.getSequence();

			// Iterate the protein sequence and check for pattern.
			while (sequence.indexOf(pattern, startIndex) != -1) {

				// Search for multiple hits
				startIndex = sequence.indexOf(pattern, startIndex);
				peptideHit.setStart(startIndex);
				endIndex = startIndex + pattern.length();
				peptideHit.setEnd(endIndex);

				// Set the found amino acid sites in the protein to true.
				for (int i = startIndex; i < endIndex; i++) {
					foundAA[i] = true;
				}
				startIndex++;

				// Search only once or not
				if (hitsCoveredOnlyOnce) {
					break;
				}
			}
			proteinHit.addPeptideHit(peptideHit);
		}

		// Number of covered amino acids.
		int nCoveredAA = 0;

		// Get the number of covered amino acids.
		for (boolean aa : foundAA) {
			if (aa) {
				nCoveredAA++;
			}
		}
		double coverage = ((double) nCoveredAA / (double) sequence.length()) * 100.0 ;
		
		return Formatter.roundDouble(coverage, 4);
	}
	
	/**
	 * Calculates the isoelectric point of the specified protein.
	 * @param proteinHit The protein.
	 * @return The isoelectric point.
	 */
	public static double calculateIsoelectricPoint(ProteinHit proteinHit) {
		Map <Character, Double> pIs = IsoelectricPoints.pIMap;
		double sum_pI = 0.0;
		for (char aa : proteinHit.getSequence().toCharArray()) {
			Double pI = pIs.get(aa);
			if (pI != null) {
				sum_pI += pI;
			}
			// TODO: find a way to deal with unknown chars
		}
		sum_pI /= proteinHit.getSequence().length();
		
	    return sum_pI;
	}
	
	/**
	 * Calculates label-free quantification measures.
	 * @param qm The quantification method object.
	 * @param params Variable argument list of parameters.
	 * @return The result of the quantification calculation.
	 */
	public static double calculateLabelFree(QuantMethod qm, Object... params) {
		qm.calculate(params);
		return qm.getResult();
	}
	
	
	
}
